# Git Bisect Plan for JDK-8376400

## Situation

- **Java 17 GA (jdk-17-ga)**: Unknown if bug exists
- **Commit 3ffec3a50b7**: Bug NOT reproducible (user tested)
- **Java 17.0.18 (dfacda488bf)**: Bug IS reproducible (confirmed)

## Bisect Strategy

Since the bug is not reproducible at `3ffec3a50b7` but IS reproducible in Java 17.0.18, we need to:

1. **Find where the bug first appears** between Java 17 GA and Java 17.0.18
2. Use git bisect to narrow it down

## Commits to Test

### Option 1: Bisect between jdk-17-ga and dfacda488bf (Java 17.0.18)

```bash
cd openjdk-bisect
git bisect start
git bisect good jdk-17-ga        # Assume no bug (or test first)
git bisect bad dfacda488bf       # Known to have bug
# Then test commits as bisect suggests
```

### Option 2: Test key commits manually first

Since building takes time, test these commits first:

1. **jdk-17-ga** - Java 17 initial release
2. **dfacda488bf** (jdk-17+35) - Java 17.0.18 (known to have bug)
3. **Midpoint commits** - Test a few in between

## Script

Use `./bisect-openjdk.sh` which will:
- Automatically bisect between jdk-17-ga and the target commit
- Build each commit
- Test with reproducer
- Mark as good/bad based on test results

## Manual Bisect

If you prefer manual control:

```bash
cd openjdk-bisect
git bisect start
git bisect good jdk-17-ga
git bisect bad dfacda488bf

# For each commit git suggests:
# 1. Build it
# 2. Test with: ./test-custom-build.sh
# 3. Mark result:
#    git bisect good  # if bug NOT present
#    git bisect bad   # if bug IS present
#    git bisect skip  # if build/test fails
```

## Expected Outcome

Find the first commit where the bug appears, which will help identify:
- What change introduced the bug
- Whether it can be backported to Java 17
- The root cause
