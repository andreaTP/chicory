// 
// Decompiled by Procyon v0.6.0
// 

package com.dylibso.chicory.wabt;

import com.dylibso.chicory.runtime.Memory;
import com.dylibso.chicory.runtime.Instance;

final class Wat2WasmModuleMachineMachineCall
{
    public Wat2WasmModuleMachineMachineCall() {
    }
    
    public static long[] call(final Instance instance, final Memory memory, final int n, final long[] array) {
        switch (n >> 12) {
            default: {
                return Wat2WasmModuleMachineDispatch_0.call_dispatch_0(instance, memory, n, array);
            }
        }
    }
}
