package esb.flows;

import esb.flows.technical.data.Hotel;
import esb.flows.technical.data.HotelReservation;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static esb.flows.technical.utils.Endpoints.*;

public class HotelErrorTest extends ActiveMQTest {
    HotelReservation hotelReq, hotelReq2;


    //on initialise les requetes de tests
    @Before
    public void initRequests() {
        hotelReq = new HotelReservation();
        hotelReq2 = new HotelReservation();


        hotelReq.setDate("28/11/2017");
        hotelReq.setDestination("Lyon");

        hotelReq2.setDate("999999999");
        hotelReq2.setDestination("123");

    }

    @Override
    public String isMockEndpointsAndSkip() {
        return HOTELSERVICE_ENDPOINTA +
                "|" + HOTELSERVICE_ENDPOINTB
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return AGGREG_HOTEL +
                "|" + RETRIEVE_A_HOTELA +
                "|" + RETRIEVE_A_HOTELB +
                "|" + DEATH_POOL
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");


    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTA);
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_A_HOTELA);
        isAvailableAndMocked(RETRIEVE_A_HOTELB);
        isAvailableAndMocked(AGGREG_HOTEL);
    }


    @Test
    public void testFakeHotelResponseFromBothServices() throws Exception {

        mock(HOTELSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(HOTELSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        //on envoit la requete au service A
        template.sendBody(RETRIEVE_A_HOTELA,hotelReq);

        //on vérifie que le endpoint a bien recu le message de RETRIEVEA
        mock(HOTELSERVICE_ENDPOINTA).assertIsSatisfied();

        //on recupere la reponse, et on crée la requete attendu
        Hotel expectedHotelA = new Hotel();
        Hotel responseHotelA = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(0).getIn().getBody();
        expectedHotelA.setDestination("err");
        expectedHotelA.setName("err");


        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedHotelA.getName(), responseHotelA.getName());
        assertEquals(expectedHotelA.getDestination(), responseHotelA.getDestination());

        mock(RETRIEVE_A_HOTELB).assertIsSatisfied();


        template.sendBody(RETRIEVE_A_HOTELB, hotelReq);


        Hotel expectedCarB = new Hotel();
        Hotel responseCarB = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(1).getIn().getBody();

        expectedCarB.setDestination("err");
        expectedCarB.setName("err");

        assertEquals(expectedCarB.getName(), responseCarB.getName());
        assertEquals(expectedCarB.getDestination(), responseCarB.getDestination());
    }

    @Test
    public void TestFakeHotelFromA() throws Exception {
        //Dire au service A de planter
        mock(HOTELSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(HOTELSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "  \"city\": \"Ipaba\",\n" +
                    "  \"name\": \"Lockman and Sons\",\n" +
                    "  \"roomCost\": 51\n" +
                    "}]";
            exc.getIn().setBody(req);
        });
        //Le service B n'est pas down et on lui envoi une requete
        template.sendBody(RETRIEVE_A_HOTELB, hotelReq);
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_A_HOTELA, hotelReq);

        //Construction d'une requête fakeAvion
        Hotel expectedHotelA = new Hotel();
        Hotel responseHotelA = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(1).getIn().getBody();
        expectedHotelA.setName("err");
        expectedHotelA.setDestination("err");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedHotelA.getName(), responseHotelA.getName());
        assertEquals(expectedHotelA.getDestination(), responseHotelA.getDestination());


        //On attend une réponse normale
        Hotel expectedCarB = new Hotel();
        Hotel responseCarB = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(0).getIn().getBody();
        expectedCarB.setDestination("Ipaba");
        expectedCarB.setName("Lockman and Sons");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedCarB.getName(), responseCarB.getName());
        assertEquals(expectedCarB.getDestination(), responseCarB.getDestination());
    }

    @Test
    public void TestFakeHotelFromB() throws Exception {
        //Dire au service B de planter
        mock(HOTELSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        mock(HOTELSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "  \"date\": \"28/11/2017\",\n" +
                    "  \"price\": 30,\n" +
                    "  \"name\": \"Hotel4\",\n" +
                    "  \"destination\": \"Paris\"\n" +
                    "}]";
            exc.getIn().setBody(req);
        });
        //Declencher le service A pour qu'il envoi une réponse
        template.sendBody(RETRIEVE_A_HOTELB, hotelReq);

        template.sendBody(RETRIEVE_A_HOTELA, hotelReq);

        //Construction d'une requête fakeAvion
        Hotel expectedHotelA = new Hotel();
        Hotel responseHotelA = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(1).getIn().getBody();
        expectedHotelA.setDestination("Paris");
        expectedHotelA.setName("Hotel4");

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedHotelA.getName(), responseHotelA.getName());
        assertEquals(expectedHotelA.getDestination(), responseHotelA.getDestination());

        //Le service B n'est pas down et on lui envoi une requete

        //On attend une réponse normale
        Hotel expectedHotelB = new Hotel();
        Hotel responseHotelB = (Hotel) mock(AGGREG_HOTEL).getReceivedExchanges().get(0).getIn().getBody();
        expectedHotelB.setDestination("err");
        expectedHotelB.setName("err");


        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedHotelB.getName(), responseHotelB.getName());
        assertEquals(expectedHotelB.getDestination(), responseHotelB.getDestination());
    }
}
