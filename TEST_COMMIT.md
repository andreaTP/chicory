# Commit to Test for Bug Reproduction

## Recommended Commit

**Commit:** `dfacda488bf` (tag: `jdk-17+35`)
**This corresponds to Java 17.0.18 which we know has the bug.**

## To Test

```bash
# Build this commit
cd openjdk-bisect
git checkout jdk-17+35
# Build it (follow your build process)

# Test with reproducer
export JAVA_HOME=/path/to/built/jdk
./reproducer.sh
```

This commit should **definitely reproduce the bug** since:
- It's from Java 17.0.18 (jdk-17+35 tag)
- We've confirmed Java 17.0.18 has the bug
- It's a stable release tag, so it's a known good commit to test

## Alternative: Latest Java 17 Commit

If you want the absolute latest Java 17 commit before Java 18:

```bash
# Find latest Java 17 commit
git log --oneline --all --since="2022-03-01" --until="2022-04-01" | grep -v "jdk-18" | head -1
```

But `jdk-17+35` (dfacda488bf) is the safest bet since it's a known release tag.
