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

### 1. Partie Réservation d'avions
  http://localhost:9090/flyreservation-service-rest/ vous affiche la liste de TOUS les vols  
  http://localhost:9090/flyreservation-service-rest/date/destination vous affiche la liste des vols correspondants par ordre croissant de prix 
  une liste des destionations se trouve dans la classe Storage  
  
  
  
## 3. Exectuer les tests gatling

Se placer dans tests/stress  
Exécuter la commande : mvn package  
Exécuter la commande : mvn gatling:execute  
  
## 4. Executer les tests d'acceptation
  
Se placer dans tests/acceptation  
Exécuter la commande : mvn generate-sources  
Executer la commande : mvn integration-test  

