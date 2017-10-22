package esb.flows.technical;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import esb.flows.technical.data.Car;
import esb.flows.technical.data.CarRequest;
import esb.flows.technical.data.HotelReservation;
import esb.flows.technical.utils.CsvFormat;
import org.apache.camel.Exchange;
import org.apache.camel.ExecutorServiceAware;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Jeremy on 22/10/2017.
 */
public class RetrieveCar extends RouteBuilder {

    //TODO Mettre les endpoints qui vont bien + Terminer Route.

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2)


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

        from(HOTEL_QUEUE)
                .routeId("car-queue")
                .routeDescription("queue des demandes de voitures")
                .to(RETRIEVE_CAR_A)
        ;

        from(RETRIEVE_CAR_A)
                .routeId("transfert de la activemq vers le service en ressources")
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
                .marshal().json(JsonLibrary.Jackson)
                .to(CAMEL_OUTPUT_CARFILE)
                ;


    }

    private static Processor csv2Carreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map <String, Object>) exchange.getIn().getBody());
        CarRequest p = new CarRequest();
        p.setDate((String) data.get("date"));
        p.setDestination((String) data.get("destination"));
        exchange.getIn().setBody(p);
    };

    private static Processor carreq2a = (Exchange exchange) -> {
        CarRequest cr = (CarRequest) exchange.getIn().getBody();
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "dest=" + cr.getDestination() + "&date=" + cr.getDate());
        exchange.getIn().setBody(null);

    };

    private static Processor answerservicea2Car = (Exchange exchange) -> {
        Car resultat = new Car();
        resultat.setPrice(String.valueOf(Integer.MAX_VALUE));
        resultat.setName("not found");
        resultat.setDate("not found");
        resultat.setDestination("not foudnd");
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
}
