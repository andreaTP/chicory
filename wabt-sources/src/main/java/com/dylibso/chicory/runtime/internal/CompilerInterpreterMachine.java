/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.runtime.internal;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.InterpreterMachine;
import com.dylibso.chicory.runtime.MStack;
import com.dylibso.chicory.runtime.StackFrame;
import com.dylibso.chicory.runtime.WasmException;
import com.dylibso.chicory.wasm.ChicoryException;
import com.dylibso.chicory.wasm.types.FunctionType;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilerInterpreterMachine
extends InterpreterMachine {
    private static final HashSet<Integer> usedInterpretedFunctions = Boolean.parseBoolean(System.getProperty("chicory.compiler.printUseOfInterpretedFunctions")) ? new HashSet() : null;
    Set<Integer> interpretedFuncIds;

    public CompilerInterpreterMachine(Instance instance, int[] interpretedFuncIds) {
        super(instance);
        this.interpretedFuncIds = Arrays.stream(interpretedFuncIds).boxed().collect(Collectors.toSet());
    }

    @Override
    protected long[] call(MStack stack, Instance instance, Deque<StackFrame> callStack, int funcId, long[] args, FunctionType callType, boolean popResults) throws ChicoryException {
        if (usedInterpretedFunctions != null && !usedInterpretedFunctions.contains(funcId)) {
            usedInterpretedFunctions.add(funcId);
            System.err.println("Chicory: calling interpreted function " + funcId);
        }
        return super.call(stack, instance, callStack, funcId, args, callType, popResults);
    }

    @Override
    protected void CALL(InterpreterMachine.Operands operands) {
        Instance instance = this.instance();
        int funcId = (int)operands.get(0);
        if (this.interpretedFuncIds.contains(funcId) || instance.function(funcId) == null) {
            super.CALL(operands);
        } else {
            MStack stack = this.stack();
            int typeId = instance.functionType(funcId);
            FunctionType type = instance.type(typeId);
            long[] args = CompilerInterpreterMachine.extractArgsForParams(stack, type.params());
            try {
                long[] results = instance.getMachine().call(funcId, args);
                if (results != null) {
                    for (long result : results) {
                        stack.push(result);
                    }
                }
            }
            catch (WasmException e) {
                StackFrame stackFrame = new StackFrame(instance, funcId, args);
                CompilerInterpreterMachine.THROW_REF(instance, instance.registerException(e), stack, stackFrame, this.callStack);
            }
        }
    }

    @Override
    protected boolean useCurrentInstanceInterpreter(Instance instance, Instance refInstance, int funcId) {
        return refInstance.equals(instance) && this.interpretedFuncIds.contains(funcId);
    }
}

