#!/bin/bash

# Wrapper for git bisect:
# - We want git to treat "bug present" as GOOD (0) and "bug fixed" as BAD (1)
#   so that the first BAD commit it finds is the FIX commit.

/home/andreatp/workspace/chicory4/bisect.sh
res=$?

# If the custom test reports SUCCESS (0 = bug NOT present),
# tell git-bisect this is a BAD commit (fix present).
if [ $res -eq 0 ]; then
  exit 1
fi

# Otherwise (non-zero = bug present or build failed),
# treat it as GOOD for bisect's purposes.
exit 0

