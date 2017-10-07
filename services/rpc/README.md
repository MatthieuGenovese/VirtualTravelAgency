## Creating the Maven project

We implement this service using the Java language, and use Maven to support the project description. The descriptor is located in the `pom.xml` file, and inherits its content from the global one described in the `service` directory (mainly the application server configuration).  The file system hierarchy is the following:

```
azrael:rpc mosser$ tree .
.
├── README.md
├── pom.xml
└── src
    └── main
        ├── java
        │   └── # service code goes here
        └── webapp
            └── WEB-INF
                └── web.xml
```

## Developing the service

### Declaring the interface

Le service déclare une opération dans l'interface [TravelAnswer](https://github.com/MatthieuGenovese/VirtualTravelAgency/blob/master/services/rpc/src/main/java/submittravel/service/TravelDecisionService.java) qui correspond à une méthode de réponse de requete de voyage.
The service declares 2 operations in the 
```java
@WebService(name="TravelDecision", targetNamespace = "http://informatique.polytech.unice.fr/soa1/cookbook/")
public interface TravelDecisionService {

    @WebResult(name="result")
    TravelAnswer answer(@WebParam(name="request") TravelRequest request,@WebParam(name="answer") boolean answer);

}
```

Les classes de demande de réponse associées sont disponible dans le packet [data](https://github.com/MatthieuGenovese/VirtualTravelAgency/tree/master/services/rpc/src/main/java/submittravel/data) package.

### Implementing the interface

L'interface est implémentée dans la class [TravelDecisionImpl](https://github.com/MatthieuGenovese/VirtualTravelAgency/blob/master/services/rpc/src/main/java/submittravel/service/TravelDecisionImpl.java) .

## Starting the service

  * Compiling: `mvn clean package` will create the file `target/submittravel-service-rpc.war`
  * Running: `mvn tomee:run` will deploy the created `war` inside a TomEE+ server, available on `localhost:8080`
  * The WSDL interface is available at [http://localhost:8080/submittravel-service-rpc/ExternalSubmitTravelService?wsdl](http://localhost:8080/submittravel-service-rpc/ExternalSubmitTravelService?wsdl)

## Explication du résultat obtenu

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

Le document se compose des sections suivantes:

    * L'élément racine (ligne 1 et 8): La déclaration namespace sur l'élément racine montre que tous les éléments et attributs non qualifié proviennent du shéma WSDL
    Le préfixe soap indique les schéma SOAP , tandis que le préfixe xsd indique les type de définition XML W3C....
    
    * Ensuite (ligne 2 et 3) , on a la définitions des messages WSDL. Ceux-ci correspondent à une demande et une réponse.
    
    * Ensuite nous avons un portType qui est équivalent à une définition interface. Ici on peut voir qu'il y a une seul opéation qui est answer . Cette opération prend en imput un message answer et retourne en output answerResponse
    
    * Maintenant que nous avons une interface ( portType ), nous pouvons définir les protocoles sur lesquels cette interface peut être consultée. 
    L'élément de liaison  crée une liaison, appelée ExternalSubmitTravelServiceSoapBinding , entre TravelDecision et SOAP. 
    Étant donné que SOAP peut fonctionner avec une variété de transports  et qu'il peut fonctionner de manière centrée sur RPC ou centrée sur le document, les attributs sur le "soap: binding" indiquent qu'il s'agit d'une liaison de type RPC utilisant HTTP.
    
    * Enfin, une intance du service est définie dans l'éléments "WSDL:service" 
    Un WSDL service contient une liste déléments de port WSDL.Chaque port définit une instance spécifique d'un serveur conforme à l'une des liaisons WSDL définies précédemment 
    