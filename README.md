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
    
  L'ordre des requetes n'a pas d'importance, les résultats seront toujours par ordre croissant de prix


### 2. Partie Réservation de vols

  http://localhost:9080/flightreservation-service-document/registry pour accéder au service
  
  Header requis :
    - Content-Type : application/json

  Requêtes POST

  Envoyer des fichiers JSON en body pour utiliser l'application (exemples de fichiers dans le readme de services/document)
  
  
### 3. Partie Soumettre le voyage 


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


## 5. Choix de conception pour les différentes interface de service

**Paradigme Document :**

Nous avons choisi d'appliquer l'interface Document pour la réservation d'avion. Ce choix permet à l'utilisateur de
de se détacher des procédures utilisées pour se concentrer sur les paramètres qui l'intéresse, ici définis par les
 différents filtres qu'il souhaite appliquer sur sa demande de réservation d'avion. Le paradigme Document nous parait être
 la meilleure structure, nous apportant assez de liberté pour gérer la complexité amenée par le nombre variable de filtres
 souhaités par l'utilisateur.

 **Paradigme Ressources :**

 Nous avons choisi d'utiliser le paradigme *Ressources* comme interface du service de réservation de voitures et d'hôtels.
 La réservation de voitures et d'hôtels ne prenant en compte que deux paramètres, une date et un lieu, nous avons décidé de les
 lier. On peut dès lors voir ces objets comme des ressources auxquelles on accederait par une URL. Ce paradigme nous offre
 aussi une grande modularité, on pourrait ajouter des chemins très aisément afin de compléter ce service de réservation en
 changeant la ressource demandée à la base de l'URI et en complétant à chaque fois avec un champ date et un champ lieu.

 **Paradigme RPC :**

 Enfin, nous avons groupé l'envoi d'une demande de validation à un supérieur et l'envoi du mail de confirmation. Ces
 modules n'ayant pas, à priori, à subir d'évolutions à court terme, pourront donc être très rigides en terme de contrat.
 L'utilisateur fera  toujours appel à la même procédure pour demander une validation.

 ## 6. Discussion

 Ces affectations n'étaient pas nos premiers choix. En effet, nous avons d'abord attribué l'interface *Document* pour la
 réservation d'hôtel et de voiture, afin que l'utilisateur ne se préoccupe que d'envoyer les paramètres qui l'intéresse
 sans se soucier de la méthode qui allait être utilisée pour lui renvoyer le résultat  attendu.
 Finalement, cette flexibilité de l'interface *Document* sied mieux à la réservation d'avion : le nombre
 de paramètres variables est plus handicapant que le simple paramètre voiture/hôtel.

 Aussi, nous avons attaché l'interface *Ressource* à la réservation d'avion, très vite nous avons compris les limitations
 de cette interface dans ce cas précis : chemin  qu'on ne peut pas manipuler librement, impossibilité de mettre des valeurs
 par défaut, l'utilsateur peut mettre les chemins dans n'importe quel ordre etc....

 Enfin, après avoir attribué l'interface *Document* au service de réservation d'avion, nous avons découvert que ce
 paradigme se prêtait bien aux discussions asynchrones idéales pour traiter la validation des demandes à son manager.
 Cependant, le paradigme *RPC* aurait été néfaste si rattaché au service de réservation d'avion (Nombre élevé de
 procédures : l'utilisateur devant toutes les connaitre avec l'ordre d'appel des filtres etc...), nous avons donc décidé de
 garder l'interface *Document* pour le service de réservation d'avion et d'avoir une interface un peu plus rigide pour
 la demande de validation et l'envoi de mail de confirmation.