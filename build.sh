#!/bin/sh

PUSH=true
#PUSH=false

cd services

build() { # $1: directory, $2: image_name
  cd $1
  docker build -t $2 .
  if [ "$PUSH" = "true" ]; then docker push $2; fi
  cd ..
}

# Compile services code
mvn -q clean package

# Build docker images
build rpc       travelagency/submittravel-rpc
build resource  travelagency/cars-hotels-reservation-rest
build document  travelagency/flightreservation-document
