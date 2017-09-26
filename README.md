# VirtualTravelAgency


## 1. Prérequis

-Être sous linux  
-Avoir java JDK 1.8 installé  
-Avoir maven d'installé >> sudo apt-get install maven  
-Avoir docker d'installé (voir le tuto >> https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/)  
-Avoir docker-compose d'installé (voir le tuto >> https://docs.docker.com/compose/install/#install-compose)  

  
## 2. Executer le programme

Vérifier que le service docker est bien lancé (service --status-all) si ce n'est pas le cas : sudo service docker start  
Se placer dans le dossier services du projet puis éxecuter le build.sh  
Se placer dans le dossier deployment du projet puis éxecuter la commande : sudo docker-compose up -d  
"Normalement" le serveur tourne apr-s ca, pour vérifier : ouvrer un navigateur et tappez http://localhost:9080  
  
## 3. Exectuer les tests gatling

Se placer dans tests/stress  
Exécuter la commande : mvm package  
Exécuter la commande : mvn gatling:execute  
  
## 4. Executer les tests d'acceptation
  
Se placer dans tests/acceptation  
Exécuter la commande : mvm generate-sources  
Executer la commande : mvn integration-test  

