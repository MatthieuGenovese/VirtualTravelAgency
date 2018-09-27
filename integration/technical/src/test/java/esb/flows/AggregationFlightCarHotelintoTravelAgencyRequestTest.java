package esb.flows;

import esb.flows.technical.data.Car;
import esb.flows.technical.data.Flight;
import esb.flows.technical.data.Hotel;
import esb.flows.technical.data.TravelAgencyRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static esb.flows.technical.utils.Endpoints.*;

public class AggregationFlightCarHotelintoTravelAgencyRequestTest extends ActiveMQTest{
    Flight flightReq1;
    Hotel hotelReq1;
    Car carReq1;
    TravelAgencyRequest travelRequest;



    @Override
    public String isMockEndpointsAndSkip() {
        return REQUETE_QUEUE;
    }

    @Override
    public String isMockEndpoints() {
        return DEATH_POOL +
                "|" + AGGREG_TRAVELREQUEST
                ;
    }

    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(AGGREG_TRAVELREQUEST);
        isAvailableAndMocked(REQUETE_QUEUE);
        isAvailableAndMocked(DEATH_POOL);
    }


    @Before
    public void initRequests(){
        flightReq1 = new Flight();
        hotelReq1 = new Hotel();
        carReq1 = new Car();
        travelRequest = new TravelAgencyRequest();

        flightReq1.setPrice("222");
        flightReq1.setDate("21-10-2017");
        flightReq1.setDestination("Paris");

        carReq1.setDate("21-10-2017");
        carReq1.setDestination("Paris");
        carReq1.setName("toto");
        carReq1.setPrice("300");

        hotelReq1.setDestination("Paris");
        hotelReq1.setName("tata");
        hotelReq1.setPrice("200");

        travelRequest.setCarReq(carReq1);
        travelRequest.setFlightReq(flightReq1);
        travelRequest.setHotelReq(hotelReq1);
    }


    //On vérifie qu'une fois 2 requetes récupérées, l'aggregateur renvoit uniquement la moins chere des 2
    @Test
    public void testAggreg3ItemsIntoTravelRequest() throws Exception {
        mock(REQUETE_QUEUE).expectedMessageCount(1);
        mock(AGGREG_TRAVELREQUEST).expectedMessageCount(3);
        Map<String, Object> headers = new HashMap<>();
        headers.put("type","flight");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, flightReq1,headers);

        headers.clear();
        headers.put("type","car");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, carReq1, headers);

        headers.clear();
        headers.put("type","hotel");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, hotelReq1, headers);

        mock(AGGREG_TRAVELREQUEST).assertIsSatisfied();
        mock(REQUETE_QUEUE).assertIsSatisfied();

        TravelAgencyRequest responseReq = (TravelAgencyRequest)  mock(REQUETE_QUEUE).getReceivedExchanges().get(0).getIn().getBody();

        Flight responseFlight = responseReq.getFlightReq();

        assertEquals(flightReq1.getDate(), responseFlight.getDate());
        assertEquals(flightReq1.getDestination(), responseFlight.getDestination());
        assertEquals(flightReq1.getPrice(), responseFlight.getPrice());

        Car responseCar = responseReq.getCarReq();

        assertEquals(carReq1.getDate(), responseCar.getDate());
        assertEquals(carReq1.getDestination(), responseCar.getDestination());
        assertEquals(carReq1.getPrice(), responseCar.getPrice());

        Hotel responseHotel = responseReq.getHotelReq();

        assertEquals(hotelReq1.getName(), responseHotel.getName());
        assertEquals(hotelReq1.getDestination(), responseHotel.getDestination());
        assertEquals(hotelReq1.getPrice(), responseHotel.getPrice());

        // template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);

        mock(DEATH_POOL).assertIsSatisfied();
    }
}
