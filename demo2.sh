#!/bin/sh


curl -i \
    -H "Content-Type: application/json" \
    -X POST -d '{ "event": "REGISTER", "flightreservation": { "id":"1", "destination":"Paris",  "date":"2017-10-12", "isDirect":"false", "price":"200", "stops":["Marseille", "Toulouse"] } }'\
    http://localhost:9080/flightreservation-service-document/registry
