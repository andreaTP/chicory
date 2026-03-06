package com.dylibso.chicory.nativepoc;

import com.dylibso.chicory.compiler.MachineFactoryCompiler;
import com.dylibso.chicory.log.SystemLogger;
import com.dylibso.chicory.runtime.ImportValues;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.wasi.WasiOptions;
import com.dylibso.chicory.wasi.WasiPreview1;
import com.dylibso.chicory.wasm.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.util.List;

/**
 * PoC: Full pipeline — Cranelift (running inside Chicory) compiles Wasm to native x86_64,
 * then Panama executes the native code. Zero native libraries shipped.
 */
public class NativePoc {

    public static void main(String[] args) throws Throwable {
        int a = 17;
        int b = 25;
        int expected = a + b;

        System.out.println("=== Chicory Native PoC ===");
        System.out.println("Testing: add(" + a + ", " + b + ") = " + expected);
        System.out.println();

        // --- 1. Chicory Interpreter ---
        System.out.println("--- Chicory Interpreter ---");
        var wasmBytes = NativePoc.class.getResourceAsStream("/add.wasm").readAllBytes();
        var module = Parser.parse(wasmBytes);
        var interpInstance = Instance.builder(module).build();
        var interpAdd = interpInstance.export("add");
        long[] interpResult = interpAdd.apply(a, b);
        System.out.println("Result: " + interpResult[0]);
        assert interpResult[0] == expected : "Interpreter result mismatch!";
        System.out.println("OK!");
        System.out.println();

        // --- 2. Chicory AOT Compiler ---
        System.out.println("--- Chicory AOT Compiler ---");
        var aotInstance =
                Instance.builder(module)
                        .withMachineFactory(MachineFactoryCompiler::compile)
                        .build();
        var aotAdd = aotInstance.export("add");
        long[] aotResult = aotAdd.apply(a, b);
        System.out.println("Result: " + aotResult[0]);
        assert aotResult[0] == expected : "AOT result mismatch!";
        System.out.println("OK!");
        System.out.println();

        // --- 3. Compile add.wasm to native using Cranelift (running inside Chicory!) ---
        System.out.println("--- Cranelift-in-Chicory compilation ---");
        byte[] nativeElf = compilWithCraneliftInChicory(wasmBytes, "x86_64-unknown-linux-gnu");
        System.out.println("  Cranelift produced " + nativeElf.length + " bytes of ELF");

        // Extract function[0] from the .text section
        byte[] funcBytes = extractTextSection(nativeElf);
        System.out.println("  Extracted " + funcBytes.length + " bytes from .text section");
        System.out.println();

        // --- 4. Execute native code via Panama ---
        System.out.println("--- Panama Native (Cranelift-in-Chicory compiled) ---");
        int nativeResult = callNativeAdd(funcBytes, a, b);
        System.out.println("Result: " + nativeResult);
        assert nativeResult == expected : "Native result mismatch!";
        System.out.println("OK!");
        System.out.println();

        System.out.println("=== All four paths produced correct results! ===");
        System.out.println("  Cranelift compiled Wasm to native x86_64 INSIDE Chicory,");
        System.out.println("  then Panama executed the native code. Zero native libs.");
    }

    /**
     * Run the Cranelift compiler (itself a Wasm module) inside Chicory
     * to compile a Wasm module to native code.
     */
    static byte[] compilWithCraneliftInChicory(byte[] wasmToCompile, String target)
            throws Exception {
        var craneliftWasm =
                NativePoc.class.getResourceAsStream("/cranelift-compiler.wasm").readAllBytes();
        var craneliftModule = Parser.parse(craneliftWasm);

        var stdin = new ByteArrayInputStream(wasmToCompile);
        var stdout = new ByteArrayOutputStream();
        var stderr = new ByteArrayOutputStream();

        var wasiOpts =
                WasiOptions.builder()
                        .withStdin(stdin)
                        .withStdout(stdout)
                        .withStderr(stderr)
                        .withArguments(List.of("cranelift-compiler", target))
                        .build();

        try (var wasi =
                WasiPreview1.builder()
                        .withLogger(new SystemLogger())
                        .withOptions(wasiOpts)
                        .build()) {
            var imports = ImportValues.builder().addFunction(wasi.toHostFunctions()).build();
            Instance.builder(craneliftModule).withImportValues(imports).build();
        }

        var stderrStr = stderr.toString();
        if (!stderrStr.isEmpty()) {
            System.out.println("  Cranelift stderr: " + stderrStr.trim());
        }

        return stdout.toByteArray();
    }

    /**
     * Extract just the first function's code from the .text section of the ELF.
     * Minimal ELF parser — finds .text section header and reads its contents.
     * Only extracts the first function (up to the second symbol).
     */
    static byte[] extractTextSection(byte[] elf) {
        // Find .text section by scanning section headers
        // ELF64 header: e_shoff at offset 0x28 (8 bytes), e_shentsize at 0x3A (2 bytes),
        // e_shnum at 0x3C (2 bytes), e_shstrndx at 0x3E (2 bytes)
        long shOff = readLong(elf, 0x28);
        int shEntSize = readShort(elf, 0x3A);
        int shNum = readShort(elf, 0x3C);
        int shStrNdx = readShort(elf, 0x3E);

        // Get the section name string table
        long strTabOff = readLong(elf, (int) (shOff + shStrNdx * shEntSize + 0x18));

        // Find .text section
        for (int i = 0; i < shNum; i++) {
            int shStart = (int) (shOff + i * shEntSize);
            int nameIdx = readInt(elf, shStart);
            String name = readString(elf, (int) (strTabOff + nameIdx));
            if (".text".equals(name)) {
                long offset = readLong(elf, shStart + 0x18);
                long size = readLong(elf, shStart + 0x20);
                // Find the first function size by looking at the second symbol
                // For now, just return enough bytes for the add function (12 bytes)
                // A proper implementation would parse the symbol table
                byte[] text = new byte[(int) size];
                System.arraycopy(elf, (int) offset, text, 0, (int) size);
                return text;
            }
        }
        throw new RuntimeException("No .text section found in ELF");
    }

    static long readLong(byte[] data, int offset) {
        long val = 0;
        for (int i = 7; i >= 0; i--) {
            val = (val << 8) | (data[offset + i] & 0xFF);
        }
        return val;
    }

    static int readInt(byte[] data, int offset) {
        return (data[offset] & 0xFF)
                | ((data[offset + 1] & 0xFF) << 8)
                | ((data[offset + 2] & 0xFF) << 16)
                | ((data[offset + 3] & 0xFF) << 24);
    }

    static int readShort(byte[] data, int offset) {
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
    }

    static String readString(byte[] data, int offset) {
        int end = offset;
        while (end < data.length && data[end] != 0) end++;
        return new String(data, offset, end - offset);
    }

    static int callNativeAdd(byte[] codeBytes, int a, int b) throws Throwable {
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

        MemorySegment codeAddr =
                (MemorySegment)
                        mmap.invoke(
                                MemorySegment.NULL,
                                pageSize,
                                PROT_READ | PROT_WRITE,
                                MAP_PRIVATE | MAP_ANONYMOUS,
                                -1,
                                0L);

        var writableCode = codeAddr.reinterpret(pageSize);
        MemorySegment.copy(MemorySegment.ofArray(codeBytes), 0, writableCode, 0, codeBytes.length);

        mprotect.invoke(codeAddr, pageSize, PROT_READ | PROT_EXEC);

        // Cranelift calling convention: rdi=vmctx, rsi=caller_vmctx, rdx=a, rcx=b
        var funcDescriptor =
                FunctionDescriptor.of(
                        ValueLayout.JAVA_INT,
                        ValueLayout.ADDRESS,
                        ValueLayout.JAVA_LONG,
                        ValueLayout.JAVA_INT,
                        ValueLayout.JAVA_INT);

        MethodHandle nativeAdd = linker.downcallHandle(writableCode, funcDescriptor);

        int result = (int) nativeAdd.invoke(MemorySegment.NULL, 0L, a, b);

        munmap.invoke(codeAddr, pageSize);

        return result;
    }
}
