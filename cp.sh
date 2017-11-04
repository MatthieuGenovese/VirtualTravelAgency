#!/bin/sh


cp test2Flight.csv ..
cp test2Hotel.csv ..
cp test2Car.csv ..
cp test2Manager.csv ..

cp test2Flight.csv deployment/camel_input
cp test2Hotel.csv deployment/camel_input
cp test2Car.csv deployment/camel_input
cp test2Manager.csv deployment/camel_input
cp test2Spend.csv deployment/camel_input

