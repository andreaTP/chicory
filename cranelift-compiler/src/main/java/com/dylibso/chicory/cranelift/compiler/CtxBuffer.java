package com.dylibso.chicory.cranelift.compiler;

/**
 * Defines the layout of the shared context buffer (ctxBuffer) used to pass
 * data between Java and native compiled code.
 *
 * <p>The buffer is a flat byte array allocated off-heap via Panama's Arena.
 * Native code accesses it through the ctxPtr parameter (second arg of every
 * compiled function). Java code accesses it through MemorySegment get/set.
 *
 * <pre>
 * Offset  Type   Field             Description
 * ──────  ─────  ────────────────  ──────────────────────────────────────────
 *   0     i64    funcTablePtr      Pointer to function pointer table
 *   8     i64    trampolinePtr     Upcall stub for CALL_INDIRECT fallback
 *  16     i32    trapCode          Trap code written by native pre-checks
 *  20     i32    typeId            CALL_INDIRECT: expected type index
 *  24     i32    tableIdx          CALL_INDIRECT: table index
 *  28     i32    elemIdx           CALL_INDIRECT: table element index
 *  32     i32    argCount          Arg count for calls; grow delta for memory.grow
 *  36     ---    (padding)
 *  40     i64[]  args              Up to 20 call arguments (widened to i64)
 * 200     i64    globalsPtr        Pointer to globals buffer
 * 208     i64    memGrowPtr        Upcall stub for memory.grow
 * 216     i32    memoryPages       Current memory page count
 * 220     ---    (padding)
 * 224     i64    memBaseAddr       Current memory base address
 * ──────  ─────  ────────────────  ──────────────────────────────────────────
 * Total: 232 bytes used, 256 allocated (CTX_SIZE)
 * </pre>
 */
final class CtxBuffer {

    private CtxBuffer() {}

    /** Total allocated size of the context buffer. */
    static final int CTX_SIZE = 256;

    // --- Fixed pointer slots (written once at init) ---

    /** Pointer to the function pointer table (one i64 per function). */
    static final int FUNC_TABLE_PTR = 0;

    /** Pointer to the CALL_INDIRECT trampoline upcall stub. */
    static final int TRAMPOLINE_PTR = 8;

    // --- Trap reporting ---

    /** Trap code written by native pre-check blocks. 0 = no trap. */
    static final int TRAP_CODE = 16;

    // --- CALL_INDIRECT metadata ---

    /** Expected type index for indirect call type check. */
    static final int TYPE_ID = 20;

    /** Table index for indirect call. */
    static final int TABLE_IDX = 24;

    /** Element index within the table. */
    static final int ELEM_IDX = 28;

    // --- Call arguments ---

    /** Argument count (also used as grow delta for memory.grow). */
    static final int ARG_COUNT = 32;

    /** Base offset for call arguments (each i64, up to 20). */
    static final int ARGS_BASE = 40;

    /** Maximum number of call arguments that fit in the buffer. */
    static final int MAX_ARGS = 20;

    // --- Memory and globals ---

    /** Pointer to the off-heap globals buffer. */
    static final int GLOBALS_PTR = 200;

    /** Pointer to the memory.grow upcall stub. */
    static final int MEM_GROW_PTR = 208;

    /** Current memory page count (i32). */
    static final int MEMORY_PAGES = 216;

    /** Current memory base address (i64). */
    static final int MEM_BASE_ADDR = 224;

    // --- Trap codes (values written to TRAP_CODE offset) ---

    static final int TRAP_NONE = 0;
    static final int TRAP_DIV_BY_ZERO = 1;
    static final int TRAP_INT_OVERFLOW = 2;
    static final int TRAP_UNREACHABLE = 3;
    static final int TRAP_TRUNC_OVERFLOW = 4;

    /** Returns the byte offset for the i-th call argument. */
    static int argOffset(int i) {
        return ARGS_BASE + 8 * i;
    }
}
