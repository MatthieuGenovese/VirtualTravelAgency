#!/bin/bash

declare -a tableau=("submittravel" "cars-hotels-reservation" "flightreservation" "travel-cars" "hotels-team1" "vols-document" "spends")
size=${#tableau[@]}

num=$(($RANDOM % $size))
echo "Stopping container ${tableau[$num]}"
docker stop ${tableau[$num]}
sleep 10
echo "Restarting container ${tableau[$num]}"
docker start ${tableau[$num]}
