package com.dylibso.chicory.runtime;

import com.dylibso.chicory.wasm.types.MutabilityType;
import com.dylibso.chicory.wasm.types.ValType;

/**
 * Factory for creating {@link GlobalInstance} objects during module instantiation.
 * Allows native compilers to substitute off-heap global storage.
 */
@FunctionalInterface
public interface GlobalFactory {
    GlobalInstance create(long value, long highValue, ValType type, MutabilityType mutability);
}
