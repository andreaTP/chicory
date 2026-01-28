#!/usr/bin/env bash
set -euo pipefail

# Minimal wabt reproducer for the MemCopyWorkaround / Temurin 17 issue.
# Usage:
#   sdk use java 17.0.16-tem
#   ./reproducer.sh

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "[reproducer] Building wabt and dependencies (no tests)..."
mvn -q -Pdev -pl wabt -am \
  -DskipTests \
  package test-compile

echo "[reproducer] Running Wat2WasmReproducerMain..."
# Use Maven exec plugin which handles classpath automatically
mvn -q -pl wabt exec:java \
  -Dexec.mainClass="com.dylibso.chicory.wabt.Wat2WasmReproducerMain" \
  -Dexec.classpathScope=test

