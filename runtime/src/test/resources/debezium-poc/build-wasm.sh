#!/bin/bash
set -euo pipefail

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

docker run --rm \
    -v ${SCRIPT_DIR}/:/src \
    -w /src tinygo/tinygo:0.34.0 bash \
    -c "tinygo build -ldflags='-extldflags --import-memory' --no-debug -target=wasm-unknown -o /tmp/processor.wasm go/processor.go && cat /tmp/processor.wasm" > \
    ${SCRIPT_DIR}/compiled/processor.wasm
