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
  
  On peut ajouter des paramètres optionnels (date et destination) voila des exemples d'utilisation :  
  http://localhost:9090/cars-hotels-reservation-service-rest/hotels?date=28/11/2018  
  http://localhost:9090/cars-hotels-reservation-service-rest/hotels?date=28/11/2018&dest=Paris  
    
  L'ordre des requetes n'a pas d'importance, les résultats seront toujours par ordre croissant de prix.
  
  
### 2. Partie Soumettre le voyage 


  http://localhost:9070/submittravel-service-rpc/ExternalSubmitTravelService?wsdl
  ```xml
             <wsdl:definitions xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:tns="http://informatique.polytech.unice.fr/soa1/cookbook/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:ns1="http://schemas.xmlsoap.org/soap/http" name="ExternalSubmitTravelService" targetNamespace="http://informatique.polytech.unice.fr/soa1/cookbook/">
             <wsdl:types>...</wsdl:types>
             <wsdl:message name="answer">...</wsdl:message>
             <wsdl:message name="answerResponse">...</wsdl:message>
             <wsdl:portType name="TravelDecision">...</wsdl:portType>
             <wsdl:binding name="ExternalSubmitTravelServiceSoapBinding" type="tns:TravelDecision">...</wsdl:binding>
             <wsdl:service name="ExternalSubmitTravelService">...</wsdl:service>
             </wsdl:definitions>
   ```
    
      
       
        * L'élément racine (ligne 1 et 8): La déclaration namespace sur l'élément racine montre que tous les éléments et attributs non qualifié proviennent du shéma WSDL
        Le préfixe soap indique les schéma SOAP , tandis que le préfixe xsd indique les type de définition XML W3C....
        
        * Ensuite (ligne 2 et 3) , on a la définitions des messages WSDL. Ceux-ci correspondent à une demande et une réponse.
        
        * Ensuite nous avons un portType qui est équivalent à une définition interface. Ici on peut voir qu'il y a une seul opéation qui est answer . Cette opération prend en imput un message answer et retourne en output answerResponse
        
        * Maintenant que nous avons une interface ( portType ), nous pouvons définir les protocoles sur lesquels cette interface peut être consultée. 
        L'élément de liaison  crée une liaison, appelée ExternalSubmitTravelServiceSoapBinding , entre TravelDecision et SOAP. 
        Étant donné que SOAP peut fonctionner avec une variété de transports  et qu'il peut fonctionner de manière centrée sur RPC ou centrée sur le document, les attributs sur le "soap: binding" indiquent qu'il s'agit d'une liaison de type RPC utilisant HTTP.
        
        * Enfin, une intance du service est définie dans l'éléments "WSDL:service" 
        Un WSDL service contient une liste déléments de port WSDL.Chaque port définit une instance spécifique d'un serveur conforme à l'une des liaisons WSDL définies précédemment 
      
  
## 3. Exectuer les tests gatling

A la racine du projet, exécuter : sudo ./test.sh  

## 4. Executer les tests d'acceptation
  
Se placer dans tests/acceptation  
Exécuter la commande : mvn generate-sources  
Executer la commande : mvn integration-test  

