#!/bin/sh

cd integration

mvn clean package -DskipTests

cp technical/target/technical-1.0.jar ../../apache-servicemix-7.0.1/deploy/.

