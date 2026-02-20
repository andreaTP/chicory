# PGLite on Chicory - Status & Instructions

## Goal

Run PostgreSQL queries via the PGLite WASI build on the Chicory WebAssembly runtime.

## Branch

`pglite-ai-support`

## Archive Source

```
/home/andreatp/workspace/pglite4j/wasm-build/output/sdk-dist/pglite-wasi.tar.xz
```

Contains:
- `tmp/pglite/bin/pglite.wasi` (24 MB) - PostgreSQL compiled to WASI
- `tmp/pglite/bin/postgres`, `tmp/pglite/bin/initdb` - supporting binaries
- `tmp/pglite/share/postgresql/` - config, schemas, timezones
- `tmp/pglite/lib/postgresql/` - extensions (plpgsql, dict_snowball)
- `tmp/pglite/password` - password file

## WASM Exports Available

| Export | Description |
|--------|-------------|
| `pgl_initdb` | Create database cluster |
| `pgl_backend` | Initialize backend (may trap due to WASI process spawning limits) |
| `get_channel` | Get CMA (Channel Memory Access) buffer channel |
| `get_buffer_addr` | Get buffer address for wire protocol |
| `get_buffer_size` | Get buffer size |
| `interactive_write` | Write data to wire protocol |
| `interactive_read` | Read data from wire protocol |
| `interactive_one` | Process one interaction cycle |
| `use_wire` | Enable/disable wire protocol mode |
| `clear_error` | Clear error state |
| `pgl_shutdown` | Shutdown PostgreSQL |
| `pgl_closed` | Check if closed |
| `_start` | WASI entry point |

## Changes Made

### 1. `machine-tests/pom.xml`

- **wasmFile**: Updated from old `pglite-oxide` path to:
  ```
  /home/andreatp/workspace/pglite4j/wasm-build/output/sdk-dist/pglite.wasi
  ```
- **interpreterFallback**: Set to `WARN` (replaces hardcoded function indices from the old build, since the new build has different indices)

### 2. `machine-tests/src/test/java/.../MachinesTest.java`

- Test `shouldRunPGLite` now automatically extracts the `pglite-wasi.tar.xz` archive at test time
- Uses JUnit `@TempDir` for extraction, with automatic cleanup
- Copies extracted assets into ZeroFs in-memory filesystem via `com.dylibso.chicory.wasi.Files.copyDirectory()`
- No more hardcoded paths to pre-extracted files

## How to Build & Run

```bash
# 1. Build core modules first
./mvnw install -pl annotations/annotations,annotations/processor,wasm,runtime,wasi,compiler,log,wasm-tools,wasm-corpus,compiler-maven-plugin -DskipTests

# 2. Run the PGLite test
./mvnw test -pl machine-tests -Dtest=MachinesTest#shouldRunPGLite
```

Note: Step 1 compiles the 24 MB WASM binary into Java bytecode via the `chicory-compiler-maven-plugin`. Functions that exceed JVM method size limits will automatically fall back to the interpreter (with WARN-level logging).

## Current Status

- **Archive**: Verified, contains all required files
- **pom.xml**: Updated to point to new WASM binary
- **Test**: Updated to extract from archive automatically
- **Build**: NOT YET RUN - was interrupted due to another build running in a separate tab

## Known Issue (from previous runs)

`pgl_backend()` was throwing an exception. This is the function that initializes the PostgreSQL backend. In pglite-oxide (Rust reference), this trap is expected because WASI doesn't support process spawning (`OpenPipeStream`). The test catches this and continues. The new build may behave differently and needs investigation.

## Test Flow

1. Extract `pglite-wasi.tar.xz` to temp directory
2. Copy extracted files into ZeroFs in-memory filesystem
3. Set up WASI with env vars (`PGDATA`, `PGUSER`, `PREFIX`, etc.)
4. Create WASM instance with AOT-compiled `PostgresModule`
5. Call `pgl_initdb()` - creates database cluster, verify `PG_VERSION` file
6. Call `pgl_backend()` - initialize backend (may trap, caught)
7. Get CMA channel and buffer address
8. Wire protocol handshake (startup message, auth, ReadyForQuery)
9. Send SQL query (`select 1;`) via wire protocol
10. Read and parse response

## Rebuilding PGLite

To rebuild the WASI binary from source:
```bash
cd /home/andreatp/workspace/pglite4j/wasm-build
./build.sh
```
Then re-run the Chicory build steps above. The `interpreterFallback=WARN` setting means no manual function index updates are needed after a rebuild.


Feel free to use `-Pdev` ihn Maven commands for now.
