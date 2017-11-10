package esb.flows;

import esb.flows.technical.data.Car;
import esb.flows.technical.data.CarRequest;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static esb.flows.technical.utils.Endpoints.*;
import static esb.flows.technical.utils.Endpoints.AGGREG_FLIGHT;

public class CarErrorTest extends ActiveMQTest {
    private String spendsCsv;
    CarRequest carReq, carReq2;


    //on initialise les requetes de tests
    @Before
    public void initRequests() {
        carReq = new CarRequest();
        carReq2 = new CarRequest();


        carReq.setDate("28/11/2017");
        carReq.setDestination("Lyon");
        carReq.setEnd("30/11/2017");
        carReq.setSort("asc");

        carReq2.setSort("two");
        carReq2.setDate("999999999");
        carReq2.setDestination("123");

    }

    @Override
    public String isMockEndpointsAndSkip() {
        return CARSERVICE_ENDPOINTA +
                "|" + CARSERVICE_ENDPOINTB
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return AGGREG_CAR +
                "|" + RETRIEVE_CAR_A +
                "|" + RETRIEVE_CAR_B +
                "|" + DEATH_POOL
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");


    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(CARSERVICE_ENDPOINTA);
        isAvailableAndMocked(CARSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_CAR_A);
        isAvailableAndMocked(RETRIEVE_CAR_B);
        isAvailableAndMocked(AGGREG_CAR);
    }


    @Test
    public void testFakeCarResponseFromBothServices() throws Exception {

        mock(CARSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(CARSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        //on envoit la requete au service A
        template.sendBody(RETRIEVE_CAR_A, carReq2);

        //on vérifie que le endpoint a bien recu le message de RETRIEVEA
        mock(CARSERVICE_ENDPOINTA).assertIsSatisfied();

        //on recupere la reponse, et on crée la requete attendu
        Car expectedCarA = new Car();
        Car responseCarA = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(0).getIn().getBody();
        expectedCarA.setDestination("err");
        expectedCarA.setDate("err");
        expectedCarA.setName("err");


        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarA.getDate(), responseCarA.getDate());
        assertEquals(expectedCarA.getDestination(), responseCarA.getDestination());

        mock(RETRIEVE_CAR_B).assertIsSatisfied();


        template.sendBody(RETRIEVE_CAR_B, carReq2);


        Car expectedCarB = new Car();
        Car responseCarB = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(1).getIn().getBody();

        expectedCarB.setDestination("err");
        expectedCarB.setDate("err");
        expectedCarB.setName("err");

        assertEquals(expectedCarB.getDate(), responseCarB.getDate());
        assertEquals(expectedCarB.getDestination(), responseCarB.getDestination());
    }

    @Test
    public void TestFakeCarFromA() throws Exception {
        //Dire au service A de planter
        mock(CARSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(CARSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "        \"id\": 64,\n" +
                    "            \"make\": \"Hyundai\",\n" +
                    "            \"model\": \"Elantra\",\n" +
                    "            \"year\": 1998,\n" +
                    "            \"agency\": {\n" +
                    "        \"name\": \"Feedspan\",\n" +
                    "                \"address\": \"34779 Harper Street\",\n" +
                    "                \"city\": \"Fontenay-sous-Bois\",\n" +
                    "                \"country\": \"Lyon\"\n" +
                    "    },\n" +
                    "        \"bookings\": [\n" +
                    "        {\n" +
                    "            \"id\": \"5295aa7d-1ec8-4f44-82b7-4f3284689169\",\n" +
                    "                \"start\": \"28/11/2017\",\n" +
                    "                \"end\": \"2017-07-30T01:49:14Z\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "        priceperday: 32.8\n" +
                    "    }]";
            exc.getIn().setBody(req);
        });
        //Le service B n'est pas down et on lui envoi une requete
        template.sendBody(RETRIEVE_CAR_B, carReq);
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_CAR_A, carReq);

        //Construction d'une requête fakeAvion
        Car expectedCarA = new Car();
        Car responseCarA = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(1).getIn().getBody();
        expectedCarA.setDestination("err");
        expectedCarA.setDate("err");
        expectedCarA.setName("err");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarA.getDate(), responseCarA.getDate());
        assertEquals(expectedCarA.getDestination(), responseCarA.getDestination());


        //On attend une réponse normale
        Car expectedCarB = new Car();
        Car responseCarB = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(0).getIn().getBody();
        expectedCarB.setDestination("Fontenay-sous-Bois");
        expectedCarB.setDate("28/11/2017");
        expectedCarB.setName("Hyundai");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarB.getDate(), responseCarB.getDate());
        assertEquals(expectedCarB.getDestination(), responseCarB.getDestination());
    }

    @Test
    public void TestFakeCarFromB() throws Exception {
        //Dire au service B de planter
        mock(CARSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(CARSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "    \"date\": \"28/11/2017\",\n" +
                    "    \"price\": 30,\n" +
                    "    \"name\": \"Car1\",\n" +
                    "    \"destination\": \"Lyon\"\n" +
                    "  }]";
            exc.getIn().setBody(req);
        });
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_CAR_B, carReq);

        template.sendBody(RETRIEVE_CAR_A, carReq);

        //Construction d'une requête fakeAvion
        Car expectedCarA = new Car();
        Car responseCarA = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(1).getIn().getBody();
        expectedCarA.setDestination("Lyon");
        expectedCarA.setDate("28/11/2017");
        expectedCarA.setName("Car1");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarA.getDate(), responseCarA.getDate());
        assertEquals(expectedCarA.getDestination(), responseCarA.getDestination());

        //Le service B n'est pas down et on lui envoi une requete

        //On attend une réponse normale
        Car expectedCarB = new Car();
        Car responseCarB = (Car) mock(AGGREG_CAR).getReceivedExchanges().get(0).getIn().getBody();
        expectedCarB.setDestination("err");
        expectedCarB.setDate("err");
        expectedCarB.setName("err");


        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarB.getDate(), responseCarB.getDate());
        assertEquals(expectedCarB.getDestination(), responseCarB.getDestination());
    }
}
