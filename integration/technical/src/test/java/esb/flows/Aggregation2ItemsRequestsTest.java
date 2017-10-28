package esb.flows;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import static esb.flows.technical.utils.Endpoints.*;

public class Aggregation2ItemsRequestsTest extends ActiveMQTest {
    Flight flightReq1, flightReq2;
    Hotel hotelReq1, hotelReq2;
    Car carReq1, carReq2;



    @Override
    public String isMockEndpointsAndSkip() {
        return AGGREG_TRAVELREQUEST
                ;
    }

    @Override
    public String isMockEndpoints() {
        return DEATH_POOL +
                "|" + AGGREG_FLIGHT +
                "|" + AGGREG_HOTEL +
                "|" + AGGREG_CAR
                ;
    }

    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(AGGREG_TRAVELREQUEST);
        isAvailableAndMocked(AGGREG_CAR);
        isAvailableAndMocked(AGGREG_FLIGHT);
        isAvailableAndMocked(AGGREG_HOTEL);
        isAvailableAndMocked(DEATH_POOL);
    }


    @Before
    public void initRequests(){
        flightReq1 = new Flight();
        hotelReq1 = new Hotel();
        carReq1 = new Car();
        flightReq2 = new Flight();
        hotelReq2 = new Hotel();
        carReq2 = new Car();

        flightReq1.setPrice("222");
        flightReq1.setDate("21-10-2017");
        flightReq1.setDestination("Paris");
        flightReq2.setPrice("330");
        flightReq2.setDate("21-10-2017");
        flightReq2.setDestination("Paris");

        carReq1.setDate("21-10-2017");
        carReq1.setDestination("Paris");
        carReq1.setName("toto");
        carReq1.setPrice("300");
        carReq2.setDate("21-10-2017");
        carReq2.setDestination("Paris");
        carReq2.setName("tutu");
        carReq2.setPrice("333");

        hotelReq1.setDestination("Paris");
        hotelReq1.setName("tata");
        hotelReq1.setPrice("200");
        hotelReq2.setDestination("Paris");
        hotelReq2.setName("toutou");
        hotelReq2.setPrice("222");
    }


    //On vérifie qu'une fois 2 requetes récupérées, l'aggregateur renvoit uniquement la moins chere des 2
    @Test
    public void testRetrive2Items() throws Exception {

        mock(AGGREG_FLIGHT).expectedMessageCount(2);
        mock(AGGREG_CAR).expectedMessageCount(2);
        mock(AGGREG_HOTEL).expectedMessageCount(2);
        mock(AGGREG_TRAVELREQUEST).expectedMessageCount(3);

        template.sendBody(AGGREG_FLIGHT, flightReq1);
        template.sendBody(AGGREG_FLIGHT, flightReq2);


        mock(AGGREG_FLIGHT).assertIsSatisfied();

        template.sendBody(AGGREG_CAR, carReq1);
        template.sendBody(AGGREG_CAR, carReq2);


        mock(AGGREG_CAR).assertIsSatisfied();

        template.sendBody(AGGREG_HOTEL, hotelReq1);
        template.sendBody(AGGREG_HOTEL, hotelReq2);


        mock(AGGREG_HOTEL).assertIsSatisfied();

        mock(AGGREG_TRAVELREQUEST).assertIsSatisfied();

        Flight responseFlight = (Flight)  mock(AGGREG_TRAVELREQUEST).getReceivedExchanges().get(0).getIn().getBody();

        assertEquals(flightReq1.getDate(), responseFlight.getDate());
        assertEquals(flightReq1.getDestination(), responseFlight.getDestination());
        assertEquals(flightReq1.getPrice(), responseFlight.getPrice());

        Car responseCar = (Car)  mock(AGGREG_TRAVELREQUEST).getReceivedExchanges().get(1).getIn().getBody();

        assertEquals(carReq1.getDate(), responseCar.getDate());
        assertEquals(carReq1.getDestination(), responseCar.getDestination());
        assertEquals(carReq1.getPrice(), responseCar.getPrice());

        Hotel responseHotel = (Hotel)  mock(AGGREG_TRAVELREQUEST).getReceivedExchanges().get(2).getIn().getBody();

        assertEquals(hotelReq1.getName(), responseHotel.getName());
        assertEquals(hotelReq1.getDestination(), responseHotel.getDestination());
        assertEquals(hotelReq1.getPrice(), responseHotel.getPrice());

        // template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);

        mock(DEATH_POOL).assertIsSatisfied();
    }
}
