package esb.flows;

import esb.flows.technical.data.CarRequest;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.HotelReservation;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static esb.flows.technical.utils.Endpoints.*;

public class FailInputsFromCsvTest extends ActiveMQTest{

    private String fakeRequete;


    //on initialise les requetes de tests
    @Before
    public void initRequests(){
        fakeRequete = "totovaalaplage\n" +
                "totovaalecole";

    }

    @Override
    public String isMockEndpointsAndSkip() {
        return  HOTEL_QUEUE+
                "|" + FLIGHT_QUEUE +
                "|" + CAR_QUEUE +
                "|" + ANSWER_MANAGER +
                "|" + SPENDSERVICE_ENDPOINT;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return  FILE_INPUT_HOTEL +
                "|" + FILE_INPUT_FLIGHT +
                "|" + FILE_INPUT_CAR +
                "|" + FILE_INPUT_MANAGER +
                "|" + FILE_INPUT_SPEND +
                "|" + FILE_INPUT_SPEND_MANAGER+
                "|" + DEATH_POOL;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();
    }


    @Test
    public void execContext() throws Exception{
        // Asserting endpoints existence
        assertNotNull(context.hasEndpoint(FILE_INPUT_SPEND));
        assertNotNull(context.hasEndpoint(FILE_INPUT_SPEND_MANAGER));
        assertNotNull(context.hasEndpoint(SPENDSERVICE_ENDPOINT));
        assertNotNull(context.hasEndpoint(FILE_INPUT_FLIGHT));
        assertNotNull(context.hasEndpoint(FLIGHT_QUEUE));
        assertNotNull(context.hasEndpoint(FILE_INPUT_HOTEL));
        assertNotNull(context.hasEndpoint(HOTEL_QUEUE));
        assertNotNull(context.hasEndpoint(FILE_INPUT_CAR));
        assertNotNull(context.hasEndpoint(CAR_QUEUE));
        assertNotNull(context.hasEndpoint(ANSWER_MANAGER));
        assertNotNull(context.hasEndpoint(FILE_INPUT_MANAGER));
        assertNotNull(context.hasEndpoint(DEATH_POOL));
    }

    @Test
    public void testsMultiplesInputTransformations() throws Exception {

        mock(FLIGHT_QUEUE).expectedMessageCount(0);
        mock(CAR_QUEUE).expectedMessageCount(0);
        mock(HOTEL_QUEUE).expectedMessageCount(0);
        mock(ANSWER_MANAGER).expectedMessageCount(0);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(0);
        mock(DEATH_POOL).expectedMessageCount(6);

        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2Flight.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2Car.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2Hotel.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2Manager.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2Spend.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", fakeRequete, Exchange.FILE_NAME, "test2MSpend.csv");


        mock(FLIGHT_QUEUE).assertIsSatisfied();
        mock(HOTEL_QUEUE).assertIsSatisfied();
        mock(CAR_QUEUE).assertIsSatisfied();
        mock(ANSWER_MANAGER).assertIsSatisfied();
        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();
        mock(DEATH_POOL).assertIsSatisfied();


        String answerSpend = mock(DEATH_POOL).getReceivedExchanges().get(0).getIn().getHeader("err").toString();
        String answerCar = mock(DEATH_POOL).getReceivedExchanges().get(1).getIn().getHeader("err").toString();
        String answerHostel = mock(DEATH_POOL).getReceivedExchanges().get(2).getIn().getHeader("err").toString();
        String answerFlight = mock(DEATH_POOL).getReceivedExchanges().get(3).getIn().getHeader("err").toString();
        String answerSpendManager = mock(DEATH_POOL).getReceivedExchanges().get(4).getIn().getHeader("err").toString();
        String answerApprobation = mock(DEATH_POOL).getReceivedExchanges().get(5).getIn().getHeader("err").toString();
        assertEquals("failinput", answerSpend);
        assertEquals("failinput", answerCar);
        assertEquals("failinput", answerHostel);
        assertEquals("failinput", answerFlight);
        assertEquals("failinput", answerSpendManager);
        assertEquals("failinput", answerApprobation);
    }

}
