#!/bin/bash

echo "Ajout de vols dans la BD du service reservations d'avions"

curl -i \
    -H "Content-Type: application/json" \
    -X POST -d "{ "event":"PURGE", "use_with":"caution" }" \
    http://localhost:9080/flightreservation-service-document/registry

curl -i \
    -H "Content-Type: application/json" \
    -X POST -d '{ "event": "REGISTER", "flightreservation": { "id":"1", "destination":"Paris",  "date":"2017-10-12", "isDirect":"false", "price":"500", "stops":["Marseille", "Toulouse"] } }'\
    http://localhost:9080/flightreservation-service-document/registry

curl -i \
    -H "Content-Type: application/json" \
    -X POST -d '{ "event": "REGISTER", "flightreservation": { "id":"2", "destination":"Paris",  "date":"2017-10-12", "isDirect":"false", "price":"400", "stops":["Marseille", "Toulouse"] } }'\
    http://localhost:9080/flightreservation-service-document/registry

curl -i \
    -H "Content-Type: application/json" \
    -X POST -d '{ "event": "REGISTER", "flightreservation": { "id":"3", "destination":"Paris",  "date":"2017-10-12", "isDirect":"false", "price":"300", "stops":["Marseille", "Toulouse"] } }'\
    http://localhost:9080/flightreservation-service-document/registry

i=0
while [[ $i -lt 30 ]]
do
    echo "Copie de fichiers utilisateurs"
    ./sendTests.sh test2Car.csv
    ./sendTests.sh test2Flight.csv
    ./sendTests.sh test2Hotel.csv
    ./sendTests.sh test2Manager.csv
    ./sendTests.sh test2Spend.csv
    ./sendTests.sh test2MSpend.csv

    sleep 5

    echo "Copie de fichiers utilisateurs mal formatés"
    ./sendTests.sh test2CarFail.csv
    ./sendTests.sh test2FlightFail.csv
    ./sendTests.sh test2HotelFail.csv
    ./sendTests.sh test2ManagerFail.csv
    ./sendTests.sh test2SpendFail.csv
    ./sendTests.sh test2MSpendFail.csv

    sleep 5

    ((i++))
    echo "Nouveau tour $i"
done
