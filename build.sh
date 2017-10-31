#!/bin/sh

#PUSH=true
PUSH=false


mvn clean package -DskipTests

cd services

build() { # $1: directory, $2: image_name
  cd $1
  docker build -t $2 .
  if [ "$PUSH" = "true" ]; then docker push $2; fi
  cd ..
}

# Compile services code

# Build docker images
build rpc       travelagency/submittravel-rpc
build resource  travelagency/cars-hotels-reservation-rest
build document  travelagency/flightreservation-document
build document2 travelagency/spends-document

cd ../integration

build esb travelagency/esb

cd ..

build integration travelagency/travelagency-bus
