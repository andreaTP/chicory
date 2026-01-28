# WABT MemCopyWorkaround Reproducer

This directory contains a minimal reproducer for the `JDK-8376400` issue that occurs on Temurin 17.

## Quick Start

### Option 1: Using the Shell Script

```bash
sdk use java 17.0.16-tem
mvn -Dquickly
./reproducer.sh
```

while running something like:

```bash
sdk use java 25-tem
./reproducer.sh
```

returns the expected result.

### Option 2: Using Docker

```bash
docker build -f wabt/Dockerfile -t wabt-reproducer .
docker run wabt-reproducer
```

changing the base image to `FROM eclipse-temurin:25` returns the expected result.
