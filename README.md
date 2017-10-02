# VirtualTravelAgency


## 1. Prérequis

-Être sous linux  
-Avoir java JDK 1.8 installé  
-Avoir maven d'installé >> sudo apt-get install maven  
-Avoir docker d'installé (voir le tuto >> https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/)  
-Avoir docker-compose d'installé (voir le tuto >> https://docs.docker.com/compose/install/#install-compose)  

  
## 2. Executer le programme

Vérifier que le service docker est bien lancé (service --status-all) si ce n'est pas le cas : sudo service docker start  
A la racine du projet, exécuter la commande : sudo ./start.sh

### 1. Partie Réservation d'hotels / voitures
  http://localhost:9090/cars-hotels-reservation-service-rest/hotels vous affiche la liste de TOUS les hotels  
  http://localhost:9090/cars-hotels-reservation-service-rest/cars vous affiche la liste de TOUTES les voitures  
  
  
  
## 3. Exectuer les tests gatling

A la racine du projet, exécuter : sudo ./test.sh  

## 4. Executer les tests d'acceptation
  
Se placer dans tests/acceptation  
Exécuter la commande : mvn generate-sources  
Executer la commande : mvn integration-test  

