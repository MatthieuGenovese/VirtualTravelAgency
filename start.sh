#!/bin/sh

cd services

sudo ./build.sh

cd ../deployment

sudo docker-compose up -d