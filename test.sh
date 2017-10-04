#!/bin/sh

cd tests

mvn clean package

cd stress

mvn gatling:execute