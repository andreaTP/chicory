#!/bin/bash

set -ex

cd /home/andreatp/workspace/jdk

git bisect start
git bisect good dfacda488bf      # older commit, bug present
git bisect bad  2ba5cc4163c      # newer commit, bug fixed
git bisect run /home/andreatp/workspace/chicory4/bisect-test.sh
git bisect reset

