package esb.flows.technical;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import esb.flows.technical.data.Car;
import esb.flows.technical.data.CarRequest;
import esb.flows.technical.utils.CsvFormat;
import esb.flows.technical.utils.FlightCarHotelAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.ExecutorServiceAware;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

/**
 * Created by Jeremy on 22/10/2017.
 */
public class RetrieveCar extends RouteBuilder {

    //TODO Mettre les endpoints qui vont bien + Terminer Route.

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2);


    @Override
    public void configure() throws Exception {
        from(FILE_INPUT_CAR)
                .routeId("csv-to-retrieve-car")
                .routeDescription("Récuperer une voiture a partir de sa destination et de sa date")
                .unmarshal(CsvFormat.buildCsvFormat())
                .split(body())
                .parallelProcessing().executorService(WORKERS)
                .process(csv2Carreq)
                .log("car csv -> requete")
                .to(CAR_QUEUE)
        ;

        from(CAR_QUEUE)
                .routeId("car-queue")
                .routeDescription("queue des demandes de voitures")
                .multicast()
                .to(RETRIEVE_CAR_A, RETRIEVE_CAR_B)
        ;

        from(RETRIEVE_CAR_A)
                .routeId("transfert de la activemq vers le serviceA en ressources")
                .routeDescription("trans")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader("Accept", constant("application/json"))

                .log("Reception des cars du serviceA")
                .process(carreq2a)
                .inOut(CARSERVICE_ENDPOINTA)
                .log(CARSERVICE_ENDPOINTA)
                .unmarshal().string()
                .log("MARSHAL")
                .process(answerservicea2Car)
                //.marshal().json(JsonLibrary.Jackson)
                .to(AGGREG_CAR)
                ;

        from(RETRIEVE_CAR_B)
                .routeId("transfert de la activemq vers le serviceB en ressources")
                .routeDescription("trans")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader("Accept",constant("application/json"))

                .log("Reception des cars serviceB")
                .process(carreq2b)
                .inOut(CARSERVICE_ENDPOINTB)
                .log(CARSERVICE_ENDPOINTB)
                .unmarshal().string()
                .process(answerserviceb2Car)
                //.marshal().json(JsonLibrary.Jackson)
                .to(AGGREG_CAR)
                ;

        from(AGGREG_CAR)
                .routeId("aggreg-car")
                .routeDescription("Aggregation des cars")
                .log("Before agreg" + body())
                .aggregate(constant(true), new FlightCarHotelAggregationStrategy())
                    .completionSize(2)
                .log("After agreg" + body())
                .marshal().json(JsonLibrary.Jackson)
                .to(CAMEL_OUTPUT_CARFILE)
                ;





    }

    private static Processor csv2Carreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map <String, Object>) exchange.getIn().getBody();
        CarRequest p = new CarRequest();
        p.setDate((String) data.get("date"));
        p.setDestination((String) data.get("destination"));
        p.setEnd((String) data.get("end"));
        p.setSort((String) data.get("sort"));
        exchange.getIn().setBody(p);
    };

    private static Processor carreq2a = (Exchange exchange) -> {
        CarRequest cr = (CarRequest) exchange.getIn().getBody();
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "dest=" + cr.getDestination() + "&date=" + cr.getDate());
        exchange.getIn().setBody(null);


    };

    private static Processor carreq2b = (Exchange exchange) -> {
        CarRequest cr = (CarRequest) exchange.getIn().getBody();
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "dest=" + cr.getDestination() + "&date=" + cr.getDate() + "&end" + cr.getEnd() + "&sort=" + cr.getSort());
        exchange.getIn().setBody(null);
    };

    /*
    [
  {
    "date": "28/11/2017",
    "price": 60,
    "name": "Car1",
    "destination": "Lyon"
  },
  {
    "date": "28/11/2017",
    "price": 70,
    "name": "Car3",
    "destination": "Paris"
  },
  {
    "date": "28/12/2017",
    "price": 80,
    "name": "Car2",
    "destination": "Paris"
  }
]
     */
    private static Processor answerservicea2Car = (Exchange exchange) -> {
        Car resultat = new Car();
        resultat.setPrice(String.valueOf(Integer.MAX_VALUE));
        resultat.setName("not found");
        resultat.setDate("not found");
        resultat.setDestination("not found");
        try {
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse((String) exchange.getIn().getBody());
            JsonArray json = obj.getAsJsonArray();
            for(JsonElement j : json){
                JsonObject jsontmp = j.getAsJsonObject();
                if(Integer.valueOf(jsontmp.get("price").getAsString()) < Integer.valueOf(resultat.getPrice())){
                    resultat.setDestination(jsontmp.get("destination").getAsString());
                    resultat.setName(jsontmp.get("name").getAsString());
                    resultat.setDate(jsontmp.get("date").getAsString());
                    resultat.setPrice(jsontmp.get("price").getAsString());
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
            exchange.getIn().setBody(resultat);
        }
        exchange.getIn().setBody(resultat);
    };

   /* {
        "id": 64,
            "make": "Hyundai",
            "model": "Elantra",
            "year": 1998,
            "agency": {
        "name": "Feedspan",
                "address": "34779 Harper Street",
                "city": "Fontenay-sous-Bois",
                "country": "France"
    },
        "bookings": [
        {
            "id": "5295aa7d-1ec8-4f44-82b7-4f3284689169",
                "start": "2017-05-20T17:25:35Z",
                "end": "2017-07-30T01:49:14Z"
        }
    ],
        priceperday: 32.8
    }*/

    private static Processor answerserviceb2Car = (Exchange exchange) -> {
        Car resultat = new Car();
        resultat.setPrice(String.valueOf(Integer.MAX_VALUE));
        resultat.setName("not found");
        resultat.setDate("not found");
        resultat.setDestination("not found");
        try {
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse((String) exchange.getIn().getBody());
            JsonArray json = obj.getAsJsonArray();
            for(JsonElement j : json){
                JsonObject jsontmp = j.getAsJsonObject();
                JsonObject jsonagency = jsontmp.getAsJsonObject("agency");
                JsonObject jsonbooking = jsontmp.getAsJsonObject("bookings");
                if(Integer.valueOf(jsontmp.get("priceperday").getAsString()) < Integer.valueOf(resultat.getPrice())){
                    resultat.setDestination(jsonagency.get("country").getAsString());
                    resultat.setName(jsontmp.get("make").getAsString());
                    resultat.setDate(jsonbooking.get("start").getAsString());
                    resultat.setPrice(jsontmp.get("priceperday").getAsString());
                }

            }
        }
        catch(Exception e){
            e.printStackTrace();
            exchange.getIn().setBody(resultat);
        }
        exchange.getIn().setBody(resultat);
    };
}
