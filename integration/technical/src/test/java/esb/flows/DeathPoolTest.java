package esb.flows;

        import esb.flows.technical.data.*;
        import org.apache.camel.CamelExecutionException;
        import org.apache.camel.Exchange;
        import org.junit.Before;
        import org.junit.Test;

        import java.io.IOException;
        import java.net.UnknownHostException;

        import static esb.flows.technical.utils.Endpoints.*;

public class DeathPoolTest extends ActiveMQTest {
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


        spendsCsv = "type,idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency\n" +
        "submit,1,momo,chennouf,mc154254@etu.unice.fr,01;02,45;98,resto;avion,28/06/2006;28/01/2017,AT;AT,EUR;EUR";
    }

    @Override
    public String isMockEndpointsAndSkip() {
        return SPENDSERVICE_ENDPOINT
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return FILE_INPUT_SPEND +
                "|" + DEATH_POOL +
                "|" + AGGREG_FLIGHT +
                "|" + RETRIEVE_A_FLIGHTA +
                "|" + RETRIEVE_A_FLIGHTB
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(SPENDSERVICE_ENDPOINT).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new IOException());
        });

        /*mock(RETRIEVE_A_FLIGHTB).whenAnyExchangeReceived((Exchange exc) ->{
            exc.setException(new IOException());
        });*/
    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(DEATH_POOL);
        assertNotNull(context.hasEndpoint(FILE_INPUT_SPEND));
//        isAvailableAndMocked(FILE_INPUT_SPEND);
        isAvailableAndMocked(SPENDSERVICE_ENDPOINT);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTB);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTA);
        isAvailableAndMocked(AGGREG_FLIGHT);
    }

    @Test
    public void testSpendsRouteError() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(1);
        mock(FILE_INPUT_SPEND).expectedMessageCount(1);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);
        template.sendBody(FILE_INPUT_SPEND, spendsCsv);
        mock(DEATH_POOL).assertIsSatisfied();
    }

    @Test
    public void testFakeFlightResponseFromBothServices()throws Exception{
        mock(AGGREG_FLIGHT).expectedMessageCount(2);

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
}
