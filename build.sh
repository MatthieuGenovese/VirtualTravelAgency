#!/bin/sh

#PUSH=true
PUSH=false


mvn clean package

cd services

build() { # $1: directory, $2: image_name
  cd $1
  docker build -t $2 .
  if [ "$PUSH" = "true" ]; then docker push $2; fi
  cd ..
}

# Compile services code

echo "Pulling externals services"

docker pull matthieugenovese/hotels-team1
docker pull matthieugenovese/travel-cars
docker pull matthieugenovese/document-vol

# Build docker images
echo "Building locals resources"

build rpc       travelagency/submittravel-rpc
build resource  travelagency/cars-hotels-reservation-rest
build document  travelagency/flightreservation-document
build document2 travelagency/spends-document

cd ../integration

build esb travelagency/esb

cd ..

build integration travelagency/travelagency-bus

cd deployment

mkdir camel_input
mkdir camel_output
