#!/bin/bash

rm -rf bin app.jar
mkdir -p bin

kotlinc src/main/kotlin -d bin

jar cfm app.jar MANIFEST.mf -C bin/ .
java -jar app.jar
