package esb.flows.technical;

import esb.flows.technical.data.*;
import esb.flows.technical.utils.CsvFormat;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

public class RetrieveFlight extends RouteBuilder {


    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(5);
    @Override
    public void configure() throws Exception {
        from(FILE_INPUT_DIRECTORY)
                .routeId("csv-to-retrieve-req")
                .routeDescription("Recupérer un avion a partir de son id")
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                .split(body())
                    .parallelProcessing().executorService(WORKERS)
                        .process(csv2flightreq)
                .log("je suis passé par la")
                .to(RETRIEVE_A_FLIGHT)
        ;

        from(RETRIEVE_A_FLIGHT)
                .routeId("calling-registry")
                .routeDescription("transfert de l'activemq vers le service document")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .log("j'ai recu des trucs !")
                .process(flightreq2mika)
                .inOut(FLIGHTSERVICE_ENDPOINT)
                //.unmarshal().json(JsonLibrary.Jackson, String.class)
                //.process(answermika2flight)
                //.unmarshal().json(JsonLibrary.Jackson, Flight.class)
                //.marshal().json(JsonLibrary.Jackson)
                .to(CAMEL_OUTPUT_TEST)
        ;
    }

    private static Processor csv2flightreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        FlightRequest p =  new FlightRequest();
        p.setDate((String) data.get("date"));
        p.setEvent((String) data.get("event"));
        p.setDestination((String) data.get("destination"));
        p.setIsDirect((String) data.get("direct"));
        p.setOrigine((String) data.get("origine"));
        exchange.getIn().setBody(p);
    };

    private static Processor flightreq2mika = (Exchange exchange) -> {
        FlightRequest fr = (FlightRequest) exchange.getIn().getBody();
        //{ "event": "LIST", "filter": { "destination":"Paris", "date":"2017-09-30", "stops":["Marseille", "Toulouse"] } }
        String req = "{ \"event\": \"LIST\", \"filter\": {\"destination\" : \"" +
                    fr.getDestination() +"\", \"date\":\"" + fr.getDate()+"\", \"isDirect\": " + fr.getIsDirect() +" } }";
        exchange.getIn().setBody(req);

    };
    /*{
        "size": 1,
            "vols": [{
        "date": "2017-09-30",
                "price": "200",
                "destination": "Paris",
                "id": "1",
                "stops": [
        "Marseille",
                "Toulouse"
    ],
        "isDirect": false
    }]
    }*/


    private static Processor answermika2flight = (Exchange exchange) -> {
        String tmpStr =  (String) exchange.getIn().getBody();
        String[] array = tmpStr.split("\"");
        Flight fl = new Flight();
        fl.setDate(array[7]);
        fl.setDestination(array[15]);
        fl.setPrice(array[11]);
        exchange.getIn().setBody(fl);
    };

}
