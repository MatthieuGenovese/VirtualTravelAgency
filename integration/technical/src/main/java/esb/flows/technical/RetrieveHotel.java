package esb.flows.technical;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import esb.flows.technical.data.*;
import esb.flows.technical.utils.CsvFormat;
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
                .to(RETRIEVE_A_HOTELA)
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
                //.unmarshal().string()
                .log("MARSHALL")
//                .process(answerservicea2flight)
                //.marshal().json(JsonLibrary.Jackson)
                .to(CAMEL_OUTPUT_TESTHOTEL) // on stocke la reponse (ici dans un fichier)
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
        
        
}