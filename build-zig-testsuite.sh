#! /bin/bash
set -x

ZIG_INSTALL="zig-install"
ZIG_VERSION="0.11.0" # TODO: update me

ZIG_SOURCE="zig-source"

BINARYEN_VERSION="116"
BINARYEN_INSTALL="binaryen-install"

ZIG_TESTSUITE="zig-testsuite"

# Install Zig 
if [ ! -d "$ZIG_INSTALL" ]; then
    mkdir -p ${ZIG_INSTALL}
    curl -sSL https://ziglang.org/download/${ZIG_VERSION}/zig-linux-x86_64-${ZIG_VERSION}.tar.xz | tar -xJ --strip-components=1 -C ${ZIG_INSTALL}
fi

# Install Zig source
if [ ! -d "$ZIG_SOURCE" ]; then
    mkdir -p ${ZIG_SOURCE}
    curl -sSL https://ziglang.org/download/${ZIG_VERSION}/zig-${ZIG_VERSION}.tar.xz | tar -xJ --strip-components=1 -C ${ZIG_SOURCE}
fi

#Install Binaryen
if [ ! -d "$BINARYEN_INSTALL" ]; then
    mkdir -p ${BINARYEN_INSTALL}
    curl -sSL https://github.com/WebAssembly/binaryen/releases/download/version_${BINARYEN_VERSION}/binaryen-version_${BINARYEN_VERSION}-x86_64-linux.tar.gz | tar -xz --strip-components=1 -C ${BINARYEN_INSTALL}
fi

PATH=${PWD}/${ZIG_INSTALL}:${PWD}/${BINARYEN_INSTALL}/bin:$PATH

# --test-no-exec allows building of the test Wasm binary without executing command.
# --force-link-libc attempt to fix: 719/2552 test.openat smoke test... reached unreachable code
(
    cd ${ZIG_SOURCE} && \
        zig test --test-no-exec -target wasm32-wasi --zig-lib-dir ./lib ./lib/std/std.zig --force-link-libc
)

mkdir -p ${ZIG_TESTSUITE}
# We use find because the test.wasm will be something like ./zig-cache/o/dd6df1361b2134adc5eee9d027495436/test.wasm
cp $(find ${PWD}/${ZIG_SOURCE} -name test.wasm) ${PWD}/${ZIG_TESTSUITE}/test.wasm

# The generated test binary is large and produces skewed results in favor of the optimized compiler.
# We also generate a stripped, optimized binary with wasm-opt.
wasm-opt ${PWD}/${ZIG_TESTSUITE}/test.wasm -O --strip-dwarf -o ${PWD}/${ZIG_TESTSUITE}/test-opt.wasm
