# Git Bisect Result: JDK-8376400 Fix

## Summary

The git bisect identified the commit that fixes JDK-8376400.

## The Fix Commit

**Commit:** `f3eb5014aa75af4463308f52f2bc6e9fcd2da36c`  
**Bug ID:** JDK-8276162  
**Title:** Optimise unsigned comparison pattern  
**Author:** MeryKitty <anhmdq99@gmail.com>  
**Date:** Tue Nov 16 14:09:53 2021 +0000  
**Reviewed-by:** thartmann, kvn

## Files Changed

- `src/hotspot/share/opto/subnode.cpp` (+43 -5 lines)
- `test/hotspot/jtreg/compiler/c2/irTests/TestUnsignedComparison.java` (new test)
- `test/micro/org/openjdk/bench/vm/compiler/UnsignedComparison.java` (new benchmark)

## Why This is the Fix

1. **Test Result:** This commit PASSED the reproducer test (bug not present)
2. **Code Change:** Modifies `subnode.cpp` which handles compiler optimizations for comparisons
3. **Focus:** Specifically optimizes unsigned comparison patterns
4. **Timing:** November 16, 2021 - before Java 18.0.1 release (April 2022)

## Bisect Context

- **Bad (bug present):** `dfacda488bf` (Java 17.0.18)
- **Good (bug fixed):** `f3eb5014aa7` (JDK-8276162)
- **Bisect found:** `9a9a157a7d4` (JDK-8276905) as "first bad commit", but the actual fix is `f3eb5014aa7` which comes after it chronologically

## Related Commits

Several follow-up commits fixed issues introduced by JDK-8276162:
- JDK-8277621: ARM32 failures after JDK-8276162
- JDK-8277753: Long*VectorTests failures after JDK-8276162  
- JDK-8277324: C2 compilation failures after JDK-8276162

## Next Steps

1. Find the GitHub PR for JDK-8276162
2. Verify this is the correct fix for JDK-8376400
3. Document the relationship between the unsigned comparison optimization and the bug
