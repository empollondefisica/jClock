#!/bin/bash

if [ -z "${1}" ]
then
    java -cp bin jClock
else
    java -cp bin ${*}
fi
