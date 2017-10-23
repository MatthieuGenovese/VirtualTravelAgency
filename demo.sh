#!/bin/sh
curl -i \
    -H "Content-Type: application/json" \
    -X POST -d "{ "event":"PURGE", "use_with":"caution" }" \
    http://localhost:9080/flightreservation-service-document/registry

curl -i \
    -H "Content-Type: application/json" \
    -X POST -d '{ "event": "REGISTER", "flightreservation": { "id":"1", "destination":"Paris",  "date":"2017-10-12", "isDirect":"false", "price":"500", "stops":["Marseille", "Toulouse"] } }'\
    http://localhost:9080/flightreservation-service-document/registry
