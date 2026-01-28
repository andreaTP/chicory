package com.dylibso.chicory.runtime;

//
// This class is used by compiler generated classes. It MUST remain backwards compatible
// so that older generated code can run on newer versions of the library.
//
// For the purpose of reproducing the wabt issue, we keep only the public API surface
// and strip out the original workaround implementation.
public final class MemCopyWorkaround {
    private MemCopyWorkaround() {}

    public static boolean shouldUseMemWorkaround() {
        return false;
    }

    public static boolean shouldUseMemWorkaround(String version) {
        return false;
    }

    public static void memoryCopy(int destination, int offset, int size, Memory memory) {
        memory.copy(destination, offset, size);
    }

    public static int i32_ge_u(int a, int b) {
        return (a >= b) ? 1 : 0;
    }
}
