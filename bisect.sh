#!/bin/bash

set -ex

cd /home/andreatp/workspace/jdk
source $HOME/.sdkman/bin/sdkman-init.sh
sdk use java 18.0.1-tem
bash configure --enable-warnings-as-errors=no
make images

cd /home/andreatp/workspace/chicory4
./test-custom-build.sh
