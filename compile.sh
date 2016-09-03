#!/bin/bash

if ! [ -e "bin" ]
then
    mkdir bin
fi

javac -d bin \
src/*.java
