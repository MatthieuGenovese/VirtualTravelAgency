package esb.flows;

import esb.flows.technical.data.CarRequest;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.HotelReservation;
import esb.flows.technical.data.ManagerAnswer;
import org.apache.camel.Exchange;
import org.junit.*;

import static esb.flows.technical.utils.Endpoints.*;

public class FlightCarHotelManagerInputReadingParsingTest extends ActiveMQTest {

    @Override public String isMockEndpointsAndSkip() { return ANSWER_MANAGER + "|" + FLIGHT_QUEUE + "|" + HOTEL_QUEUE + "|" + CAR_QUEUE + "|" + SPENDSERVICE_ENDPOINT; }

    @Override public String isMockEndpoints() {
        return FILE_INPUT_MANAGER + "|" + FILE_INPUT_FLIGHT + "|" + FILE_INPUT_CAR + "|" + FILE_INPUT_HOTEL + "|" +
                  FILE_INPUT_SPEND + "|" + FILE_INPUT_SPEND_MANAGER;
    }

    private String flightReq, carReq, hotelReq, managerReq, managerSpendValidateRequest, managerSpendRejectRequest;
    private String spendRetrieveRequest;
    private String answerManagerSpendValidateRequest;
    private String answerSpendRetrieveExpected, answerManagerSpendRejectRequest;


    @Before
    public void initRequest() {
        flightReq = "event,origine,destination,date,direct\n" +
                "One_Way_Price,Nice,Paris,12-10-2017,false";

        carReq = "date,destination,end,sort\n" +
                "28/11/2017,Lyon, 60,asc";

        hotelReq = "date,destination\n" +
                "28/11/2017,Ipaba\n";

        managerReq = "answer\n" +
                "1\n";

        spendRetrieveRequest = "type,idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency,justification\n" +
                "retrieve,1";

        managerSpendValidateRequest = "type,id\n" +
                "validate,1";

        managerSpendRejectRequest = "type,id\n" +
                "reject,1";

        answerSpendRetrieveExpected = "{\n" +
                "      \"type\":\"retrieve\",\n" +
                "      \"id\":\"1\"\n" +
                "}";

        answerManagerSpendRejectRequest = "{\"type\":\"reject\",\"id\":\"1\"}";

        answerManagerSpendValidateRequest = "{\"type\":\"validate\",\"id\":\"1\"}";

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
    }

    //@Test
    public void testsMultiplesInputTransformations() throws Exception {

        mock(FLIGHT_QUEUE).expectedMessageCount(1);
        mock(CAR_QUEUE).expectedMessageCount(1);
        mock(HOTEL_QUEUE).expectedMessageCount(1);
        mock(ANSWER_MANAGER).expectedMessageCount(1);

        template.sendBodyAndHeader("file:/servicemix/camel/input", flightReq, Exchange.FILE_NAME, "test2Flight.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", carReq, Exchange.FILE_NAME, "test2Car.csv");
        //template.sendBody(FILE_INPUT_HOTEL, hotelReq);
        template.sendBodyAndHeader("file:/servicemix/camel/input", hotelReq, Exchange.FILE_NAME, "test2Hotel.csv");
        template.sendBodyAndHeader("file:/servicemix/camel/input", managerReq, Exchange.FILE_NAME, "test2Manager.csv");
        //template.sendBodyAndHeader("file:/servicemix/camel/input", managerSpendRequest, Exchange.FILE_NAME, "test2SpendManager.csv");

        mock(FLIGHT_QUEUE).assertIsSatisfied();
        mock(HOTEL_QUEUE).assertIsSatisfied();
        mock(CAR_QUEUE).assertIsSatisfied();
        mock(ANSWER_MANAGER).assertIsSatisfied();

        // As the assertions are now satisfied, one can access to the contents of the exchanges
        FlightRequest reponseFlight = (FlightRequest) mock(FLIGHT_QUEUE).getReceivedExchanges().get(0).getIn().getBody();
        CarRequest reponseCar = (CarRequest) mock(CAR_QUEUE).getReceivedExchanges().get(0).getIn().getBody();
        HotelReservation reponseHotel = (HotelReservation) mock(HOTEL_QUEUE).getReceivedExchanges().get(0).getIn().getBody();
        ManagerAnswer reponseManager = (ManagerAnswer) mock(ANSWER_MANAGER).getReceivedExchanges().get(0).getIn().getBody();


        FlightRequest expectedFlight = new FlightRequest();
        HotelReservation expectedHotel = new HotelReservation();
        CarRequest expectedCar = new CarRequest();
        ManagerAnswer expectedManagerAns = new ManagerAnswer();

        expectedFlight.setEvent("One_Way_Price");
        expectedFlight.setIsDirect("false");
        expectedFlight.setDestination("Paris");
        expectedFlight.setOrigine("Nice");
        expectedFlight.setDate("12-10-2017");

        expectedCar.setDate("28/11/2017");
        expectedCar.setDestination("Lyon");
        expectedCar.setEnd(" 60");
        expectedCar.setSort("asc");

        expectedHotel.setDate("28/11/2017");
        expectedHotel.setDestination("Ipaba");

        expectedManagerAns.setReponse("1");

        assertEquals(expectedFlight.getDate(), reponseFlight.getDate());
        assertEquals(expectedFlight.getDestination(), reponseFlight.getDestination());
        assertEquals(expectedFlight.getEvent(), reponseFlight.getEvent());
        assertEquals(expectedFlight.getIsDirect(), reponseFlight.getIsDirect());
        assertEquals(expectedFlight.getOrigine(), reponseFlight.getOrigine());

        assertEquals(expectedCar.getDate(), reponseCar.getDate());
        assertEquals(expectedCar.getDestination(), reponseCar.getDestination());
        assertEquals(expectedCar.getEnd(), reponseCar.getEnd());
        assertEquals(expectedCar.getSort(), reponseCar.getSort());

        assertEquals(expectedHotel.getDate(), reponseHotel.getDate());
        assertEquals(expectedHotel.getDestination(), reponseHotel.getDestination());

        assertEquals(expectedManagerAns.getReponse(), reponseManager.getReponse());




    }

    //@Test
    public void spendRetrieveMessageInputTest() throws Exception{

        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);

        template.sendBodyAndHeader("file:/servicemix/camel/input", spendRetrieveRequest, Exchange.FILE_NAME, "test2Spend.csv");

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String answerSpend = mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);
        assertEquals(answerSpendRetrieveExpected, answerSpend);

    }

    //@Test
    public void spendValidateManagerMessageInputTest() throws Exception{
        //resetMocks();

        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);

        template.sendBodyAndHeader("file:/servicemix/camel/input", managerSpendValidateRequest, Exchange.FILE_NAME, "test2MSpend.csv");

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String answerSpend = mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);
        assertEquals(answerManagerSpendValidateRequest, answerSpend);

    }

    //@Test
    public void spendRejectManagerMessageInputTest() throws Exception{
        //resetMocks();

        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);

        template.sendBodyAndHeader("file:/servicemix/camel/input", managerSpendRejectRequest, Exchange.FILE_NAME, "test2MSpend.csv");

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String answerSpend = mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);
        assertEquals(answerManagerSpendRejectRequest, answerSpend);

    }

}
