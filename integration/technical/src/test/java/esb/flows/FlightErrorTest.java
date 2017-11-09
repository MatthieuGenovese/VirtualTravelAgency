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
    FlightRequest flightReq,flightReq2;


    //on initialise les requetes de tests
    @Before
    public void initRequests(){
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
        return ""
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return AGGREG_FLIGHT +
                "|" + RETRIEVE_A_FLIGHTA +
                "|" + RETRIEVE_A_FLIGHTB +
                "|" + FLIGHT_QUEUE
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(SPENDSERVICE_ENDPOINT).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(RETRIEVE_A_FLIGHTB);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTA);
        isAvailableAndMocked(AGGREG_FLIGHT);
    }


    @Test
    public void testFakeFlightResponseFromBothServices()throws Exception{
        mock(AGGREG_FLIGHT).expectedMessageCount(2);
        mock(RETRIEVE_A_FLIGHTA).expectedMessageCount(2);

        //on envoit la requete au service A
        template.sendBody(RETRIEVE_A_FLIGHTA,flightReq);

        //on vérifie que le endpoint a bien recu le message de RETRIEVEA
        mock(FLIGHTSERVICE_ENDPOINTA).assertIsSatisfied();

        //on recupere la reponse, et on crée la requete attendu
        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightA.setDestination("err");
        expectedFlightA.setDate("err");
        expectedFlightA.setPrice(String.valueOf(Integer.MAX_VALUE));

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());
        assertEquals(expectedFlightA.getPrice(), responseFlightA.getPrice());

        mock(RETRIEVE_A_FLIGHTB).assertIsSatisfied();

        try {
            template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);
        }catch (CamelExecutionException e){
            System.out.println(e.getCause());
        }


        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();

        expectedFlightB.setDestination("err");
        expectedFlightB.setDate("err");
        expectedFlightB.setPrice("0");

        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
        assertEquals(expectedFlightB.getPrice(), responseFlightB.getPrice());
    }

    @Test
    public void TestFakePlaneFromA() throws Exception{
        //Dire au service A de planter
        mock(RETRIEVE_A_FLIGHTA).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        //Declencher le service A pour qu'il envoi une réponse planté
        template.sendBody(RETRIEVE_A_FLIGHTA,flightReq);

        //Construction d'une requête fakeAvion
        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightA.setDestination("err");
        expectedFlightA.setDate("err");
        expectedFlightA.setPrice(String.valueOf(Integer.MAX_VALUE));

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());
        assertEquals(expectedFlightA.getPrice(), responseFlightA.getPrice());

        //Le service B n'est pas down et on lui envoi une requete
        template.sendBody(RETRIEVE_A_FLIGHTB,flightReq);

        //On attend une réponse normale
        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightB.setDestination("Paris");
        expectedFlightB.setDate("12-10-2017");
        expectedFlightB.setPrice(String.valueOf(0));

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
        assertEquals(expectedFlightB.getPrice(), responseFlightB.getPrice());


    }

    @Test
    public void TestFakePlaneB()throws Exception{
        //ON fait planter le service B
        mock(RETRIEVE_A_FLIGHTB).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });
        template.sendBody(FLIGHT_QUEUE,flightReq);

        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightB.setDestination("err");
        expectedFlightB.setDate("err");
        expectedFlightB.setPrice(String.valueOf(0));

        //on compare les champs de la requete reçu avec ceux de la requete attendu
        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
        assertEquals(expectedFlightB.getPrice(), responseFlightB.getPrice());
    }
}
