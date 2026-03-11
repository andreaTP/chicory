package com.dylibso.chicory.cranelift.compiler;

/**
 * TODO: Value stack with scope-aware restore for unreachable code handling.
 *
 * <p>Modeled after {@code compiler/src/main/java/.../TypeStack.java}:
 * <ul>
 *   <li>{@code enterScope(blockType, mergeParamIds)} — save restored stack
 *       = (current - params + mergeParamIds)</li>
 *   <li>{@code scopeRestore()} — at END after unreachable block, replace
 *       actual stack with saved restored stack</li>
 *   <li>Polymorphic pop: when stack is at frame.height and frame.unreachable,
 *       return a dummy zero value ID instead of failing</li>
 * </ul>
 *
 * <p>This is the missing piece that causes the remaining 45 Cranelift verifier
 * failures. See native-compilation-plan.md "Refactor plan" section.
 */
final class NativeValueStack {
    // TODO: implement
    private NativeValueStack() {}
}
