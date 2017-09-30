#!/bin/sh

cd tests

mvn package

cd stress

mvn gatling:execute