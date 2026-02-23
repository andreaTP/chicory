/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Memory;

final class Wat2WasmModuleMachineMachineCall {
    public static long[] call(Instance instance, Memory memory, int n, long[] lArray) {
        switch (n >> 12) {
            default:
        }
        return Wat2WasmModuleMachineDispatch_0.call_dispatch_0(instance, memory, n, lArray);
    }
}
