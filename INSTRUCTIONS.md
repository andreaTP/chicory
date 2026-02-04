# JDK-8376400 Validation Instructions

## Quick Start

```bash
# 1. Install Java 18 or 19 as boot JDK (required)
sdk install java 19.0.1-tem
sdk use java 19.0.1-tem

# 2. Verify setup
./verify-setup.sh

# 3. Run validation
./validate-and-backport.sh
```

## Important: Boot JDK Version

**Use Java 18 or 19 as boot JDK** (required by configure script).

The configure script requires Java 18 or 19 to build Java 18 source code.

**Solution:** Install and use Java 18 or 19:
```bash
sdk install java 19.0.1-tem
sdk use java 19.0.1-tem
export JAVA_HOME=$(sdk home java 19.0.1-tem)

# Or use Java 18:
sdk install java 18.0.1-tem
sdk use java 18.0.1-tem
```

## What the Script Does

1. **Builds Java 18 BEFORE fix** (commit `3ffec3a50b7`)
   - Should have the bug
   - Tests with reproducer → should FAIL

2. **Builds Java 18 WITH fix** (commit `a81c5d3a231`)
   - Should be fixed
   - Tests with reproducer → should PASS

## Files Created

- `jdk18-before-fix-build.log` - Build log (before)
- `jdk18-after-fix-build.log` - Build log (after)
- `test-before-fix.log` - Test results (should fail)
- `test-after-fix.log` - Test results (should pass)

## Troubleshooting

**Build fails with Feature.FOREIGN warnings:**
- Use Java 17 as boot JDK (see above)
- Or manually patch the source to remove the warnings

**Repository issues:**
- Run `./verify-setup.sh` to check

**Other build errors:**
- Check the build log files for details
- Ensure all dependencies are installed

## Expected Results

- **Before fix**: Test fails with "out of bounds memory access"
- **After fix**: Test passes with "Conversion succeeded"

This proves commit `a81c5d3a231` (PR #8189) fixes JDK-8376400.
