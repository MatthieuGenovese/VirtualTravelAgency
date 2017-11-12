#!/bin/bash

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
