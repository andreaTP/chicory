#!/bin/bash

docker build . -t jdk-repro

docker build . -t jdk-repro-patched --build-arg PATCH=f3eb5014aa75af4463308f52f2bc6e9fcd2da36c
