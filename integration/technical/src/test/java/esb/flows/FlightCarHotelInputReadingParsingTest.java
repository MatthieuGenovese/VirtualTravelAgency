package esb.flows;

import esb.flows.technical.data.CarRequest;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.HotelReservation;
import org.junit.*;

import static esb.flows.technical.utils.Endpoints.*;

public class FlightCarHotelInputReadingParsingTest extends ActiveMQTest {

    @Override public String isMockEndpointsAndSkip() { return FLIGHT_QUEUE + "|" + HOTEL_QUEUE + "|" + CAR_QUEUE; }

    @Override public String isMockEndpoints() {
        return FILE_INPUT_FLIGHT + "|" + FILE_INPUT_CAR + "|" + FILE_INPUT_HOTEL;
    }

    private String flightReq, carReq, hotelReq;


    @Before
    public void initRequest() {
        flightReq = "event,origine,destination,date,direct\n" +
                "One_Way_Price,Nice,Paris,12-10-2017,false";

        carReq = "date,destination,end,sort\n" +
                "28/11/2017,Lyon, 60,asc";

        hotelReq = "date,destination\n" +
                "28/11/2017,Ipaba\n";
    }

    @Test
    public void execContext() throws Exception{
        // Asserting endpoints existence
        assertNotNull(context.hasEndpoint(FILE_INPUT_FLIGHT));
        assertNotNull(context.hasEndpoint(FLIGHT_QUEUE));
        assertNotNull(context.hasEndpoint(FILE_INPUT_HOTEL));
        assertNotNull(context.hasEndpoint(HOTEL_QUEUE));
        assertNotNull(context.hasEndpoint(FILE_INPUT_CAR));
        assertNotNull(context.hasEndpoint(CAR_QUEUE));
    }

    @Test
    public void testCitizenRegistration() throws Exception {

        mock(FLIGHT_QUEUE).expectedMessageCount(1);
        mock(CAR_QUEUE).expectedMessageCount(1);
        mock(HOTEL_QUEUE).expectedMessageCount(1);


        template.sendBody(FILE_INPUT_FLIGHT, flightReq);
        template.sendBody(FILE_INPUT_CAR, carReq);
        template.sendBody(FILE_INPUT_HOTEL, hotelReq);

        mock(FLIGHT_QUEUE).assertIsSatisfied();
        mock(HOTEL_QUEUE).assertIsSatisfied();
        mock(CAR_QUEUE).assertIsSatisfied();

        // As the assertions are now satisfied, one can access to the contents of the exchanges
        FlightRequest reponseFlight = (FlightRequest) mock(FLIGHT_QUEUE).getReceivedExchanges().get(0).getIn().getBody();
        CarRequest reponseCar = (CarRequest) mock(CAR_QUEUE).getReceivedExchanges().get(0).getIn().getBody();
        HotelReservation reponseHotel = (HotelReservation) mock(HOTEL_QUEUE).getReceivedExchanges().get(0).getIn().getBody();


        FlightRequest expectedFlight = new FlightRequest();
        HotelReservation expectedHotel = new HotelReservation();
        CarRequest expectedCar = new CarRequest();

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


    }

}
