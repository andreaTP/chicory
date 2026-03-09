package com.dylibso.chicory.cranelift.compiler;

import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Machine;

/**
 * Public API for the Cranelift native compiler.
 * Use as: {@code Instance.builder(module).withMachineFactory(MachineFactoryNative::compile)}
 */
public final class NativeMachineFactory {

    private NativeMachineFactory() {}

    public static Machine compile(Instance instance) {
        return new NativeMachine(instance);
    }
}
