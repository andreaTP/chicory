#!/bin/bash
set +x

docker run --rm jdk-repro

docker run --rm jdk-repro-patched
