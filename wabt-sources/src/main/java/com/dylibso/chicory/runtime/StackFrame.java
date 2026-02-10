/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime;

import com.dylibso.chicory.runtime.CtrlFrame;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.MStack;
import com.dylibso.chicory.wasm.types.AnnotatedInstruction;
import com.dylibso.chicory.wasm.types.NameCustomSection;
import com.dylibso.chicory.wasm.types.OpCode;
import com.dylibso.chicory.wasm.types.ValType;
import com.dylibso.chicory.wasm.types.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StackFrame {
    private final List<AnnotatedInstruction> code;
    private AnnotatedInstruction currentInstruction;
    private final int funcId;
    private int pc;
    private final long[] locals;
    private final ValType[] localTypes;
    private final int[] localIdx;
    private final Instance instance;
    private final List<CtrlFrame> ctrlStack = new ArrayList<CtrlFrame>();

    public StackFrame(Instance instance, int funcId, long[] args) {
        this(instance, funcId, args, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
    }

    StackFrame(Instance instance, int funcId, long[] args, List<ValType> argsTypes, List<ValType> localTypes, List<AnnotatedInstruction> code) {
        int i;
        int i2;
        this.code = code;
        this.instance = instance;
        this.funcId = funcId;
        this.locals = Arrays.copyOf(args, ValType.sizeOf(argsTypes) + ValType.sizeOf(localTypes));
        int localsSize = argsTypes.size() + localTypes.size();
        this.localTypes = new ValType[localsSize];
        for (i2 = 0; i2 < argsTypes.size(); ++i2) {
            this.localTypes[i2] = argsTypes.get(i2);
        }
        for (i2 = 0; i2 < localTypes.size(); ++i2) {
            this.localTypes[argsTypes.size() + i2] = localTypes.get(i2);
        }
        this.localIdx = new int[localsSize];
        int j = 0;
        for (i = 0; i < localTypes.size(); ++i) {
            ValType type = localTypes.get(i);
            int idx = j + ValType.sizeOf(argsTypes);
            if (!type.equals(ValType.V128)) {
                this.locals[idx] = Value.zero(type);
                ++j;
                continue;
            }
            this.locals[idx] = Value.zero(ValType.I64);
            this.locals[idx + 1] = Value.zero(ValType.I64);
            j += 2;
        }
        j = 0;
        for (i = 0; i < this.localTypes.length; ++i) {
            this.localIdx[i] = j++;
            if (!this.localType(i).equals(ValType.V128)) continue;
            j += 2;
        }
    }

    void reset(long[] args) {
        for (int i = 0; i < this.locals.length; ++i) {
            this.setLocal(i, args[i]);
        }
        this.pc = 0;
    }

    int funcId() {
        return this.funcId;
    }

    ValType localType(int i) {
        return this.localTypes[i];
    }

    public int localIndexOf(int idx) {
        return this.localIdx[idx];
    }

    void setLocal(int i, long v) {
        this.locals[i] = v;
    }

    long local(int i) {
        return this.locals[i];
    }

    public String toString() {
        String funcName;
        NameCustomSection nameSec = this.instance.module().nameSection();
        String id = "[" + this.funcId + "]";
        if (nameSec != null && (funcName = nameSec.nameOfFunction(this.funcId)) != null) {
            id = funcName + id;
        }
        return id + "\n\tpc=" + this.pc + " locals=" + Arrays.toString(this.locals);
    }

    AnnotatedInstruction loadCurrentInstruction() {
        this.currentInstruction = this.code.get(this.pc++);
        return this.currentInstruction;
    }

    int currentPc() {
        return this.pc - 1;
    }

    boolean isLastBlock() {
        return this.currentInstruction.depth() == 0;
    }

    boolean terminated() {
        return this.pc >= this.code.size();
    }

    void pushCtrl(CtrlFrame ctrlFrame) {
        this.ctrlStack.add(ctrlFrame);
    }

    void pushCtrl(OpCode opcode, int startValues, int returnValues, int height) {
        this.ctrlStack.add(new CtrlFrame(opcode, startValues, returnValues, height));
    }

    void pushCtrl(OpCode opcode, int startValues, int returnValues, int height, int pc) {
        this.ctrlStack.add(new CtrlFrame(opcode, startValues, returnValues, height, pc));
    }

    int ctrlStackSize() {
        return this.ctrlStack.size();
    }

    CtrlFrame popCtrl() {
        CtrlFrame ctrlFrame = this.ctrlStack.remove(this.ctrlStack.size() - 1);
        return ctrlFrame;
    }

    CtrlFrame popCtrl(int n) {
        int mostRecentCallHeight = this.ctrlStack.size();
        while (this.ctrlStack.get((int)(--mostRecentCallHeight)).opCode != OpCode.CALL) {
        }
        int finalHeight = this.ctrlStack.size() - (mostRecentCallHeight + n + 1);
        CtrlFrame ctrlFrame = null;
        while (this.ctrlStack.size() > finalHeight) {
            ctrlFrame = this.popCtrl();
        }
        return ctrlFrame;
    }

    CtrlFrame popCtrlTillCall() {
        CtrlFrame ctrlFrame;
        do {
            ctrlFrame = this.popCtrl();
        } while (ctrlFrame.opCode != OpCode.CALL);
        return ctrlFrame;
    }

    void jumpTo(int newPc) {
        this.pc = newPc;
    }

    static void doControlTransfer(CtrlFrame ctrlFrame, MStack stack) {
        int i;
        int endResults = ctrlFrame.startValues + ctrlFrame.endValues;
        long[] returns = new long[endResults];
        for (i = 0; i < returns.length; ++i) {
            if (stack.size() <= 0) continue;
            returns[i] = stack.pop();
        }
        while (stack.size() > ctrlFrame.height) {
            stack.pop();
        }
        for (i = 0; i < returns.length; ++i) {
            long value = returns[returns.length - 1 - i];
            stack.push(value);
        }
    }
}

