
wasm-reduce \
  /home/andreatp/workspace/chicory4/wabt/src/main/resources/wat2wasm \
  --test=/home/andreatp/workspace/chicory4/wabt/src/main/resources/wat2wasm.test \
  --working=/home/andreatp/workspace/chicory4/wabt/src/main/resources/wat2wasm.reduced \
  --timeout=600 \
  --command="/home/andreatp/workspace/chicory4/reduce-oracle.sh wat2wasm.test"
