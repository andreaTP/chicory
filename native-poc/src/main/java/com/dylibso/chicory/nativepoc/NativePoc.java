package com.dylibso.chicory.nativepoc;

import com.dylibso.chicory.compiler.MachineFactoryCompiler;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasm.Parser;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * PoC: Java drives Cranelift (running inside Chicory) via explicit value IDs
 * to compile a Wasm function to native x86_64, then executes it via Panama.
 *
 * Tests add_and_store(a, b, addr) which adds two i32s, stores the result
 * to linear memory, and returns the sum.
 */
public class NativePoc {

    public static void main(String[] args) throws Throwable {
        int a = 17;
        int b = 25;
        int addr = 0; // memory offset to store result
        int expected = a + b;

        System.out.println("=== Chicory Native PoC (Cranelift Bridge) ===");
        System.out.println(
                "Testing: add_and_store(" + a + ", " + b + ", " + addr + ") = " + expected);
        System.out.println();

        // --- 1. Chicory Interpreter ---
        System.out.println("--- Chicory Interpreter ---");
        var wasmBytes = NativePoc.class.getResourceAsStream("/add_and_store.wasm").readAllBytes();
        var module = Parser.parse(wasmBytes);
        var interpInstance = Instance.builder(module).build();
        var interpFunc = interpInstance.export("add_and_store");
        long[] interpResult = interpFunc.apply(a, b, addr);
        System.out.println("Result: " + interpResult[0]);
        // Check memory was written
        int storedVal = interpInstance.memory().readInt(addr);
        System.out.println("Memory[" + addr + "] = " + storedVal);
        assert interpResult[0] == expected : "Interpreter result mismatch!";
        assert storedVal == expected : "Interpreter memory mismatch!";
        System.out.println("OK!");
        System.out.println();

        // --- 2. Chicory AOT Compiler ---
        System.out.println("--- Chicory AOT Compiler ---");
        var aotInstance =
                Instance.builder(module)
                        .withMachineFactory(MachineFactoryCompiler::compile)
                        .build();
        var aotFunc = aotInstance.export("add_and_store");
        long[] aotResult = aotFunc.apply(a, b, addr);
        System.out.println("Result: " + aotResult[0]);
        int aotStored = aotInstance.memory().readInt(addr);
        System.out.println("Memory[" + addr + "] = " + aotStored);
        assert aotResult[0] == expected : "AOT result mismatch!";
        assert aotStored == expected : "AOT memory mismatch!";
        System.out.println("OK!");
        System.out.println();

        // --- 3. Compile using Cranelift bridge (Java drives, Cranelift in Chicory) ---
        System.out.println("--- Cranelift Bridge Compilation ---");
        byte[] nativeCode = compileAddAndStoreWithBridge();
        System.out.println("  Cranelift produced " + nativeCode.length + " bytes of native code");
        System.out.println();

        // --- 4. Execute via Panama ---
        System.out.println("--- Panama Native Execution ---");
        int nativeResult = executeNative(nativeCode, a, b, addr);
        System.out.println("Result: " + nativeResult);
        assert nativeResult == expected : "Native result mismatch!";
        System.out.println("OK!");
        System.out.println();

        System.out.println("=== All paths produced correct results! ===");
    }

    /**
     * Use the CraneliftBridge to compile add_and_store to native code.
     * This is what NativeEmitters would do — here done inline for clarity.
     *
     * The function signature is:
     *   add_and_store(memBase: i64, a: i32, b: i32, addr: i32) -> i32
     *
     * We prepend memBase (i64 pointer to linear memory) as the first param.
     * The Wasm params (a, b, addr) follow.
     */
    static byte[] compileAddAndStoreWithBridge() {
        var bridge = new CraneliftBridge();
        bridge.init("x86_64-unknown-linux-gnu");

        bridge.createFunction();

        // Signature: (memBase: i64, a: i32, b: i32, addr: i32) -> i32
        bridge.addParamType(CraneliftBridge.TYPE_I64); // memBase
        bridge.addParamType(CraneliftBridge.TYPE_I32); // a
        bridge.addParamType(CraneliftBridge.TYPE_I32); // b
        bridge.addParamType(CraneliftBridge.TYPE_I32); // addr
        bridge.addReturnType(CraneliftBridge.TYPE_I32); // result
        bridge.buildFunction();

        // Create entry block
        int entry = bridge.createBlock();
        bridge.appendBlockParamsForFuncParams(entry);
        bridge.switchToBlock(entry);
        bridge.sealBlock(entry);

        // Get function params
        int memBase = bridge.funcParam(entry, 0); // i64 pointer
        int paramA = bridge.funcParam(entry, 1); // i32
        int paramB = bridge.funcParam(entry, 2); // i32
        int paramAddr = bridge.funcParam(entry, 3); // i32

        // sum = a + b
        int sum = bridge.emitIadd(paramA, paramB);

        // memory[addr] = sum (i32.store with offset 0)
        bridge.emitStoreI32(memBase, paramAddr, sum, 0);

        // return sum
        bridge.emitReturn(sum);

        System.out.println("  Built Cranelift IR via bridge");

        // Compile to native code
        return bridge.compile();
    }

    /**
     * mmap + mprotect + Panama downcall to execute native code.
     * Allocates off-heap memory for the Wasm linear memory.
     */
    static int executeNative(byte[] codeBytes, int a, int b, int addr) throws Throwable {
        var linker = Linker.nativeLinker();
        var lookup = linker.defaultLookup();

        var mmap =
                linker.downcallHandle(
                        lookup.find("mmap").orElseThrow(),
                        FunctionDescriptor.of(
                                ValueLayout.ADDRESS,
                                ValueLayout.ADDRESS,
                                ValueLayout.JAVA_LONG,
                                ValueLayout.JAVA_INT,
                                ValueLayout.JAVA_INT,
                                ValueLayout.JAVA_INT,
                                ValueLayout.JAVA_LONG));

        var mprotect =
                linker.downcallHandle(
                        lookup.find("mprotect").orElseThrow(),
                        FunctionDescriptor.of(
                                ValueLayout.JAVA_INT,
                                ValueLayout.ADDRESS,
                                ValueLayout.JAVA_LONG,
                                ValueLayout.JAVA_INT));

        var munmap =
                linker.downcallHandle(
                        lookup.find("munmap").orElseThrow(),
                        FunctionDescriptor.of(
                                ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_LONG));

        int PROT_READ = 0x1;
        int PROT_WRITE = 0x2;
        int PROT_EXEC = 0x4;
        int MAP_PRIVATE = 0x02;
        int MAP_ANONYMOUS = 0x20;
        long pageSize = 4096;

        // --- Allocate executable code region ---
        MemorySegment codeAddr =
                (MemorySegment)
                        mmap.invoke(
                                MemorySegment.NULL,
                                pageSize,
                                PROT_READ | PROT_WRITE,
                                MAP_PRIVATE | MAP_ANONYMOUS,
                                -1,
                                0L);

        var codeRegion = codeAddr.reinterpret(pageSize);
        MemorySegment.copy(MemorySegment.ofArray(codeBytes), 0, codeRegion, 0, codeBytes.length);
        mprotect.invoke(codeAddr, pageSize, PROT_READ | PROT_EXEC);

        // --- Allocate linear memory (off-heap, contiguous) ---
        long memSize = 65536; // 1 Wasm page
        MemorySegment memAddr =
                (MemorySegment)
                        mmap.invoke(
                                MemorySegment.NULL,
                                memSize,
                                PROT_READ | PROT_WRITE,
                                MAP_PRIVATE | MAP_ANONYMOUS,
                                -1,
                                0L);
        var linearMemory = memAddr.reinterpret(memSize);

        System.out.println(
                "  Code at: 0x"
                        + Long.toHexString(codeAddr.address())
                        + ", Memory at: 0x"
                        + Long.toHexString(memAddr.address()));

        // --- Call the native function ---
        // Signature: (memBase: ptr, a: i32, b: i32, addr: i32) -> i32
        // System V ABI: rdi=memBase, rsi=a, rdx=b, rcx=addr, return in eax
        var funcDescriptor =
                FunctionDescriptor.of(
                        ValueLayout.JAVA_INT, // return i32
                        ValueLayout.ADDRESS, // memBase (i64 ptr)
                        ValueLayout.JAVA_INT, // a
                        ValueLayout.JAVA_INT, // b
                        ValueLayout.JAVA_INT); // addr

        MethodHandle nativeFunc = linker.downcallHandle(codeRegion, funcDescriptor);
        int result = (int) nativeFunc.invoke(linearMemory, a, b, addr);

        // Verify memory was written
        int storedVal = linearMemory.get(ValueLayout.JAVA_INT, addr);
        System.out.println("Memory[" + addr + "] = " + storedVal);
        assert storedVal == result : "Native memory mismatch!";

        // Cleanup
        munmap.invoke(codeAddr, pageSize);
        munmap.invoke(memAddr, memSize);

        return result;
    }
}
