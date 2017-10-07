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

