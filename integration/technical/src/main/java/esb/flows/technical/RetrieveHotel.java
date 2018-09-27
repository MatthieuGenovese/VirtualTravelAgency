package esb.flows.technical;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import esb.flows.technical.data.*;
import esb.flows.technical.utils.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

public class RetrieveHotel extends RouteBuilder {
    
    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2);

    @Override
    public void configure() throws Exception {
        from(FILE_INPUT_HOTEL)
                .onException(IOException.class).handled(true)
                .log("erreur capturée dans la lecture utilisateur : " + body().toString())
                .setHeader("err", constant("failinput"))
                .to(DEATH_POOL)
                .end()
                .routeId("csv-to-retrieve-hotel")
                .routeDescription("Recupérer un hotel a partir de son endroit et sa date")
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                .split(body()) // on effectue un travaille en parralele sur la map >> on transforme tout ca en objet de type Flight
                    .parallelProcessing().executorService(WORKERS)
                        .process(csv2hotelreq)
                .choice()
                    .when(header("err").isNotEqualTo("failinput"))
                        .log("Transformation du csv en HotelRequest : " + body().toString())
                        .to(HOTEL_QUEUE)
                    .otherwise()
                        .log("erreur dans la requete utilisateur")
                        .process(makeFakeHotel)
                        .setHeader("type", constant("hotel"))
                        .multicast()
                            .to(DEATH_POOL, AGGREG_TRAVELREQUEST)
                .endChoice()
        ;
        
        from(HOTEL_QUEUE)
                .routeId("hotel-queue")
                .routeDescription("queue des demandes d'hotels")
                .multicast() // on multicast sur les 2 transformateurs
                    .to(RETRIEVE_A_HOTELA, RETRIEVE_A_HOTELB)
        ;
        
        from(RETRIEVE_A_HOTELA) // transforme des HotelReservation
                .onException(IOException.class).handled(true)
                    .process(makeFakeHotel)
                    .log("erreur capturée transformation en requete fictive : " + body().toString() )
                .to(AGGREG_HOTEL)
                .end()
                .routeId("calling-hotela")
                .routeDescription("transfert de l'activemq vers le service rest")
                .setHeader(Exchange.HTTP_METHOD, constant("GET")) // on choisis le type de requete (ici du POST en json)
                .setHeader("Accept", constant("application/json"))
                .process(hotelreq2a) // on transforme tous les objets de type FlightRequest en JSON correspondant pour le service demandé
                .log("transformation de HotelRequest en requete Service A : " + body().toString())
                .inOut(HOTELSERVICE_ENDPOINTA)
                .log(HOTELSERVICE_ENDPOINTA)
                .unmarshal().string()
                .process(answerservicea2hotel)
                .log("transformation de la réponse en objet Hotel : " + body().toString())
                .to(AGGREG_HOTEL) // on stocke la reponse (ici dans un fichier)
        ;
        
        from(RETRIEVE_A_HOTELB) // transforme des HotelReservation
                .onException(IOException.class).handled(true)
                    .process(makeFakeHotel)
                    .log("erreur capturée transformation en requete fictive : " + body().toString() )
                .to(AGGREG_HOTEL)
                .end()
                .routeId("calling-hotelb")
                .routeDescription("transfert de l'activemq vers le service rest")
                .setHeader(Exchange.HTTP_METHOD, constant("GET")) // on choisis le type de requete (ici du POST en json)
                .setHeader("Accept", constant("application/json"))
                .process(hotelreq2b) // on transforme tous les objets de type FlightRequest en JSON correspondant pour le service demandé
                .log("transformation de HotelRequest en requete Service B : " + body().toString())
                .inOut(HOTELSERVICE_ENDPOINTB)
                .log(HOTELSERVICE_ENDPOINTB)
                .unmarshal().string()
                .process(answerserviceb2hotel)
                .log("transformation de la réponse en objet Hotel : " + body().toString())
                .to(AGGREG_HOTEL) // on stocke la reponse (ici dans un fichier)
        ;
        
        from(AGGREG_HOTEL)
                .routeId("aggreg-hotel")
                .routeDescription("l'aggregator des hotels")
                .aggregate(constant(true), new FlightCarHotelAggregationStrategy())
                    .completionSize(2)
                .setHeader("type", constant("hotel"))
                .removeHeader(Exchange.HTTP_QUERY)
                .log("Requete la moins chère retenue : " + body().toString())
                .to(AGGREG_TRAVELREQUEST)
        ;
    }

    private static Processor makeFakeHotel = (Exchange exchange) -> {
        Hotel h = new Hotel();
        h.setDestination("err");
        h.setName("err");
        h.setPrice(String.valueOf(Integer.MAX_VALUE));
        exchange.getIn().setBody(h);

    };

    private static Processor csv2hotelreq = (Exchange exchange) -> { // fonction qui transforme la map issu du csv en objets de type FlightRequest
        try {
            Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
            HotelReservation p = new HotelReservation();
            p.setDate((String) data.get("date"));
            p.setDestination((String) data.get("destination"));
            if(p.getDate() == null || p.getDestination() == null) {
                exchange.getIn().setHeader("err", "failinput");
            }
            else{
                exchange.getIn().setBody(p);
                exchange.getIn().setHeader("requete-id", (String) data.get("id"));
            }
        }
        catch(NullPointerException e){
            exchange.getIn().setHeader("err", "failinput");
        }
    };

    private static Processor hotelreq2a = (Exchange exchange) -> { // fonction qui transforme un objet FlightRequest en json service b
        HotelReservation hr = (HotelReservation) exchange.getIn().getBody();
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "dest=" + hr.getDestination()+"&date="+hr.getDate());
        exchange.getIn().setBody(null);
    };
    
    private static Processor hotelreq2b = (Exchange exchange) -> { // fonction qui transforme un objet FlightRequest en json service b
        HotelReservation hr = (HotelReservation) exchange.getIn().getBody();
        exchange.getIn().setHeader(Exchange.HTTP_QUERY, "destination=" + hr.getDestination()+"&date="+hr.getDate());
        exchange.getIn().setBody(null);
    };
    
    private static Processor answerservicea2hotel = (Exchange exchange) -> {
        Hotel resultat = new Hotel();
        resultat.setPrice(String.valueOf(Integer.MAX_VALUE));
        resultat.setName("not found");
//        resultat.setDate("not found");
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
//                    resultat.setDate(jsontmp.get("date").getAsString());
                    resultat.setPrice(jsontmp.get("price").getAsString());
                }

            }
            System.out.println(resultat.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            exchange.getIn().setBody(resultat);
        }
        exchange.getIn().setBody(resultat);
    };
    
    private static Processor answerserviceb2hotel = (Exchange exchange) -> {
        Hotel resultat = new Hotel();
        resultat.setPrice(String.valueOf(Integer.MAX_VALUE));
        resultat.setName("not found");
//        resultat.setDate("not found");
        resultat.setDestination("not found");
        try {
            JsonParser jparser = new JsonParser();
            JsonElement obj = jparser.parse((String) exchange.getIn().getBody());
            JsonArray json = obj.getAsJsonArray();
            for(JsonElement j : json){
                JsonObject jsontmp = j.getAsJsonObject();
                if(Integer.valueOf(jsontmp.get("roomCost").getAsString()) < Integer.valueOf(resultat.getPrice())){
                    resultat.setDestination(jsontmp.get("city").getAsString());
                    resultat.setName(jsontmp.get("name").getAsString());
//                    resultat.setDate(jsontmp.get("date").getAsString());
                    resultat.setPrice(jsontmp.get("roomCost").getAsString());
                }

            }
            System.out.println(resultat.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            exchange.getIn().setBody(resultat);
        }
        exchange.getIn().setBody(resultat);
    };
        
        
}