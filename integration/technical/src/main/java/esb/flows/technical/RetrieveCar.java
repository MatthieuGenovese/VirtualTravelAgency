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
                .log("Transformation du csv en CarRequest : " + body().toString())
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
                .process(carreq2a)
                .log("transformation de CarRequest en requete Service A")
                .inOut(CARSERVICE_ENDPOINTA)
                .unmarshal().string()
                .process(answerservicea2Car)
                .log("transformation de la réponse en objet Car : " + body().toString())
                .to(AGGREG_CAR)
                ;

        from(RETRIEVE_CAR_B)
                .routeId("transfert de la activemq vers le serviceB en ressources")
                .routeDescription("trans")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader("Accept",constant("application/json"))
                .process(carreq2b)
                .log("transformation de CarRequest en requete Service B")
                .inOut(CARSERVICE_ENDPOINTB)
                .unmarshal().string()
                .process(answerserviceb2Car)
                .log("transformation de la réponse en objet Car : " + body().toString())
                .to(AGGREG_CAR)
                ;

        from(AGGREG_CAR)
                .routeId("aggreg-car")
                .routeDescription("Aggregation des cars")
                .aggregate(constant(true), new FlightCarHotelAggregationStrategy())
                    .completionSize(2)
                .log("Requete la moins chère retenue : " + body().toString())
                .setHeader("type", constant("car"))
                .removeHeader(Exchange.HTTP_QUERY)
                .to(AGGREG_TRAVELREQUEST)
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
        exchange.getIn().setHeader("requete-id", (String) data.get("id"));
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
                JsonElement jsontmp2 = jsontmp.get("agency");
                JsonElement jsontmp3 = jsontmp.get("bookings");

                if(Float.valueOf(jsontmp.get("priceperday").getAsString()) < Float.valueOf(resultat.getPrice())){
                    resultat.setDestination(jsontmp2.getAsJsonObject().get("city").getAsString());
                    resultat.setName(jsontmp.get("make").getAsString());
                    if(jsontmp3.getAsJsonArray().size() > 0) {
                        System.out.println(jsontmp3.getAsJsonArray().get(0).getAsJsonObject().get("start").getAsString());
                        resultat.setDate(jsontmp3.getAsJsonArray().get(0).getAsJsonObject().get("start").getAsString());
                    }

                    resultat.setPrice(jsontmp.get("priceperday").getAsString().split("\\.")[0]);
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
