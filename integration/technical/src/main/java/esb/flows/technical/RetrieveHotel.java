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
                .routeId("csv-to-retrieve-hotel")
                .routeDescription("Recupérer un hotel a partir de son endroit et sa date")
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                .split(body()) // on effectue un travaille en parralele sur la map >> on transforme tout ca en objet de type Flight
                    .parallelProcessing().executorService(WORKERS)
                        .process(csv2hotelreq)
                .log("hotel csv -> requete")
                .to(HOTEL_QUEUE)
        ;
        
        from(HOTEL_QUEUE)
                .routeId("hotel-queue")
                .routeDescription("queue des demandes d'hotels")
                .multicast() // on multicast sur les 2 transformateurs
                    .to(RETRIEVE_A_HOTELA, RETRIEVE_A_HOTELB)
        ;
        
        from(RETRIEVE_A_HOTELA) // transforme des HotelReservation
                .routeId("calling-hotela")
                .routeDescription("transfert de l'activemq vers le service rest")
                .setHeader(Exchange.HTTP_METHOD, constant("GET")) // on choisis le type de requete (ici du POST en json)
//                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                
                .log("j'ai recu des trucs hotels !")
                .process(hotelreq2a) // on transforme tous les objets de type FlightRequest en JSON correspondant pour le service demandé
                .inOut(HOTELSERVICE_ENDPOINTA)
                .log(HOTELSERVICE_ENDPOINTA)
                .unmarshal().string()
                .log("MARSHALL")
                .process(answerservicea2hotel)
//                .marshal().json(JsonLibrary.Jackson)
                .to(AGGREG_HOTEL) // on stocke la reponse (ici dans un fichier)
        ;
        
        from(RETRIEVE_A_HOTELB) // transforme des HotelReservation
                .routeId("calling-hotelb")
                .routeDescription("transfert de l'activemq vers le service rest")
                .setHeader(Exchange.HTTP_METHOD, constant("GET")) // on choisis le type de requete (ici du POST en json)
//                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                
                .log("j'ai recu des trucs hotels !")
                .process(hotelreq2b) // on transforme tous les objets de type FlightRequest en JSON correspondant pour le service demandé
                .inOut(HOTELSERVICE_ENDPOINTB)
                .log(HOTELSERVICE_ENDPOINTB)
                .unmarshal().string()
                .log("MARSHALL")
                .process(answerserviceb2hotel)
//                .marshal().json(JsonLibrary.Jackson)
                .to(AGGREG_HOTEL) // on stocke la reponse (ici dans un fichier)
        ;
        
        from(AGGREG_HOTEL)
                .routeId("aggreg-hotel")
                .routeDescription("l'aggregator des hotels")
                .log("before aggreg" + body())
                .aggregate(constant(true), new FlightCarHotelAggregationStrategy())
                    .completionSize(2)
                .log("after aggreg" + body())
                .marshal().json(JsonLibrary.Jackson)
                .to(CAMEL_OUTPUT_FINALHOTEL)
        ;
    }
        
    private static Processor csv2hotelreq = (Exchange exchange) -> { // fonction qui transforme la map issu du csv en objets de type FlightRequest
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        HotelReservation p =  new HotelReservation();
        p.setDate((String) data.get("date"));
        p.setDestination((String) data.get("destination"));
        exchange.getIn().setBody(p);
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