Rebuilds OpenJDK 17 twice: once at `jdk-17-ga` and once with a single cherry-picked patch (`PATCH` arg in `Dockerfile` / `build.sh`).

To reproduce locally:
- Run `./build.sh` to build `jdk-repro` (unpatched) and `jdk-repro-patched` (with the fix).
- Run `./docker-run.sh` to execute `run.sh` inside both images; it runs `wabt-999-SNAPSHOT.jar` and compares buggy vs fixed behavior (see `buggy/` and `fixed/` logs).

`buggy/replay.log` and `fixed/replay.log` are C2 replay logs for the failing method before/after the patch, and `buggy/asm.txt` vs `fixed/asm.txt` show the corresponding compiled assembly so you can diff the optimization.

The minimal patch that prevents the bug (extracted from the [bisected commit](f3eb5014aa75af4463308f52f2bc6e9fcd2da36c)) is shown below. It preserves the unsigned comparison operation, likely, preventing downstream optimizations from incorrectly transforming the unsigned semantics.

Diff:

```diff
diff --git a/src/hotspot/share/opto/subnode.cpp b/src/hotspot/share/opto/subnode.cpp
index 430bb5b447b..d8145093caf 100644
--- a/src/hotspot/share/opto/subnode.cpp
+++ b/src/hotspot/share/opto/subnode.cpp
@@ -1521,6 +1521,27 @@ Node *BoolNode::Ideal(PhaseGVN *phase, bool can_reshape) {
     }
   }
 
+  // Change "cmp (add X min_jint) (add Y min_jint)" into "cmpu X Y"
+  // This preserves the unsigned comparison semantics encoded by adding MIN_VALUE
+  if (cop == Op_CmpI &&
+      cmp1_op == Op_AddI &&
+      phase->type(cmp1->in(2)) == TypeInt::MIN &&
+      cmp2->Opcode() == Op_AddI &&
+      phase->type(cmp2->in(2)) == TypeInt::MIN) {
+    Node* ncmp = phase->transform(new CmpUNode(cmp1->in(1), cmp2->in(1)));
+    return new BoolNode(ncmp, _test._test);
+  }
+
+  // Change "cmp (add X min_jlong) (add Y min_jlong)" into "cmpu X Y"
+  if (cop == Op_CmpL &&
+      cmp1_op == Op_AddL &&
+      phase->type(cmp1->in(2)) == TypeLong::MIN &&
+      cmp2->Opcode() == Op_AddL &&
+      phase->type(cmp2->in(2)) == TypeLong::MIN) {
+    Node* ncmp = phase->transform(new CmpULNode(cmp1->in(1), cmp2->in(1)));
+    return new BoolNode(ncmp, _test._test);
+  }
+
   // Change x u< 1 or x u<= 0 to x == 0
   if (cop == Op_CmpU &&
       cmp1_op != Op_LoadRange &&
```