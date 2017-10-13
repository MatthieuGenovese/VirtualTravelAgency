package esb.flows.technical;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FillCitizenRegistry extends RouteBuilder {

    private static final String CSV_INPUT_DIRECTORY   = "file:/servicemix/camel/input";
    private static final String REGISTER_A_CITIZEN    = "activemq:registerCitizen";
    private static final String REGISTRATION_ENDPOINT = "http://localhost:9080/flightreservation-service-document/registry";
    public static final String GET_A_FLIGHT    = "activemq:getCitizenInfo";
    private static final String GET_A_FLIGHT_ENDPOINT = "http://localhost:9080/flightreservation-service-document/registry";


    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(5);
    @Override
    public void configure() throws Exception {
        from(GET_A_FLIGHT_ENDPOINT)
                .routeId("get-a-flight-from-our-service")
                .routeDescription("Recupérer un avion a partir du service document de notre équipe")
                .log("interception d'une requete")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .process((Exchange exchange) -> {
                    String request = "{\n" +
                            "  \"event\": \"RETRIEVE\",\n" +
                            "\"id\":\"1\"" +"\"\n" +
                            "}";
                    exchange.getIn().setBody(request);
                })
                .inOut(GET_A_FLIGHT_ENDPOINT)
                .unmarshal().json(JsonLibrary.Jackson, Flight.class)
        ;

    }

}
