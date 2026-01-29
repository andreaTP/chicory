
# wget https://github.com/WebAssembly/wasi-sdk/releases/download/wasi-sdk-25/wasi-sdk-25.0-x86_64-linux.tar.gz
# tar xvf wasi-sdk-25.0-x86_64-linux.tar.gz

export WASI_SDK_PATH=$PWD/wasi-sdk-25.0-x86_64-linux
export CC_wasm32_wasip1=$WASI_SDK_PATH/bin/clang
export CFLAGS_wasm32_wasip1="--sysroot=$WASI_SDK_PATH/share/wasi-sysroot"

cargo build --target wasm32-wasip1 --release
