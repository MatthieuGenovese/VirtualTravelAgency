package esb.flows;

import esb.flows.technical.data.Car;
import esb.flows.technical.data.Flight;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.Hotel;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static esb.flows.technical.utils.Endpoints.*;

public class FlightErrorTest extends ActiveMQTest {
    private String spendsCsv;
    FlightRequest flightReq, flightReq2;


    //on initialise les requetes de tests
    @Before
    public void initRequests() {
        flightReq = new FlightRequest();
        flightReq2 = new FlightRequest();


        flightReq.setEvent("One_Way_Price");
        flightReq.setIsDirect("false");
        flightReq.setDestination("Paris");
        flightReq.setOrigine("Nice");
        flightReq.setDate("12-10-2017");

        flightReq2.setEvent("two");
        flightReq2.setDate("999999999");

    }

    @Override
    public String isMockEndpointsAndSkip() {
        return FLIGHTSERVICE_ENDPOINTA +
                "|" + FLIGHTSERVICE_ENDPOINTB
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return AGGREG_FLIGHT +
                "|" + RETRIEVE_A_FLIGHTA +
                "|" + RETRIEVE_A_FLIGHTB +
                "|" + DEATH_POOL
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "POST");

        //config flight service B
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "POST");


    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTA);
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTB);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTA);
        isAvailableAndMocked(AGGREG_FLIGHT);
    }


    @Test
    public void testFakeFlightResponseFromBothServices() throws Exception {
        mock(AGGREG_FLIGHT).expectedMessageCount(2);
        mock(RETRIEVE_A_FLIGHTA).expectedMessageCount(2);

        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(FLIGHTSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        //on envoit la requete au service A
        template.sendBody(RETRIEVE_A_FLIGHTA, flightReq);

        //on vérifie que le endpoint a bien recu le message de RETRIEVEA
        mock(FLIGHTSERVICE_ENDPOINTA).assertIsSatisfied();

        //on recupere la reponse, et on crée la requete attendu
        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightA.setDestination("err");
        expectedFlightA.setDate("err");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());

        mock(RETRIEVE_A_FLIGHTB).assertIsSatisfied();


        template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);


        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();

        expectedFlightB.setDestination("err");
        expectedFlightB.setDate("err");

        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
    }

    @Test
    public void TestFakePlaneFromA() throws Exception {
        //Dire au service A de planter
        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(FLIGHTSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "{\"Flights\": {\"Outbound\": {\n" +
                    "  \"sorted_flights\": [\n" +
                    "    {\n" +
                    "      \"date\": \"12-10-2017\",\n" +
                    "      \"prix\": 450,\n" +
                    "      \"cmpny\": \"Ryanair\",\n" +
                    "      \"nb_escales\": 1,\n" +
                    "      \"destination\": \"Paris\",\n" +
                    "      \"rating\": 2.5,\n" +
                    "      \"duree\": 4,\n" +
                    "      \"id\": 3,\n" +
                    "      \"origine\": \"Nice\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"DATE\": \"12-10-2017\",\n" +
                    "  \"Number_of_Results\": 3\n" +
                    "}}}";
            exc.getIn().setBody(req);
        });
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);

        template.sendBody(RETRIEVE_A_FLIGHTA, flightReq);

        //Construction d'une requête fakeAvion
        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();
        expectedFlightA.setDestination("err");
        expectedFlightA.setDate("err");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());

        //Le service B n'est pas down et on lui envoi une requete

        //On attend une réponse normale
        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightB.setDestination("Paris");
        expectedFlightB.setDate("12-10-2017");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
    }

    @Test
    public void TestFakePlaneB() throws Exception {
        //Dire au service B de planter
        mock(FLIGHTSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "{\n" +
                    "  \"size\": 3,\n" +
                    "  \"vols\": [\n" +
                    "    {\n" +
                    "      \"date\": \"12-10-2017\",\n" +
                    "      \"price\": \"300\",\n" +
                    "      \"destination\": \"Paris\",\n" +
                    "      \"id\": \"3\",\n" +
                    "      \"stops\": [\n" +
                    "        \"Marseille\",\n" +
                    "        \"Toulouse\"\n" +
                    "      ],\n" +
                    "      \"isDirect\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"date\": \"12-10-2017\",\n" +
                    "      \"price\": \"350\",\n" +
                    "      \"destination\": \"Paris\",\n" +
                    "      \"id\": \"4\",\n" +
                    "      \"stops\": [\n" +
                    "        \"Marseille\",\n" +
                    "        \"Toulouse\"\n" +
                    "      ],\n" +
                    "      \"isDirect\": false\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"date\": \"12-10-2017\",\n" +
                    "      \"price\": \"350\",\n" +
                    "      \"destination\": \"Paris\",\n" +
                    "      \"id\": \"4\",\n" +
                    "      \"stops\": [\n" +
                    "        \"Marseille\",\n" +
                    "        \"Toulouse\"\n" +
                    "      ],\n" +
                    "      \"isDirect\": false\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            exc.getIn().setBody(req);
        });
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);

        template.sendBody(RETRIEVE_A_FLIGHTA, flightReq);

        //Construction d'une requête fakeAvion
        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();
        expectedFlightA.setDestination("Paris");
        expectedFlightA.setDate("12-10-2017");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());

        //Le service B n'est pas down et on lui envoi une requete

        //On attend une réponse normale
        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightB.setDestination("err");
        expectedFlightB.setDate("err");


        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
    }
}
