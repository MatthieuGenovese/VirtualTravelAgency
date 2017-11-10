package esb.flows;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static esb.flows.technical.utils.Endpoints.*;

public class ServicesNotFoundTest extends ActiveMQTest{
    HotelReservation hotelReq, hotelReq2, fakeReqHotel;
    CarRequest carReq, carReq2, fakeReqCar;
    FlightRequest flightReq, flightReq2, fakeReqFlight;
    Flight flight;
    Hotel hotel;
    Car car;

    @Before
    public void initRequests(){
        hotelReq = new HotelReservation();
        hotelReq2 = new HotelReservation();
        carReq = new CarRequest();
        carReq2 = new CarRequest();
        flightReq = new FlightRequest();
        flightReq2 = new FlightRequest();

        fakeReqHotel = new HotelReservation();
        fakeReqCar = new CarRequest();
        fakeReqFlight = new FlightRequest();

        hotelReq.setDate("28/11/2017");
        hotelReq.setDestination("Lyon");
        hotelReq2.setDate("28/11/2017");
        hotelReq2.setDestination("Lyon");
        carReq.setDate("28/11/2017");
        carReq.setDestination("Lyon");
        carReq.setEnd("30/11/2017");
        carReq.setSort("asc");
        carReq2.setDate("28/11/2017");
        carReq2.setDestination("Lyon");
        carReq2.setEnd("30/11/2017");
        carReq2.setSort("asc");
        flightReq.setEvent("One_Way_Price");
        flightReq.setIsDirect("false");
        flightReq.setDestination("Paris");
        flightReq.setOrigine("Nice");
        flightReq.setDate("12-10-2017");
        flightReq2.setEvent("One_Way_Price");
        flightReq2.setIsDirect("false");
        flightReq2.setDestination("Paris");
        flightReq2.setOrigine("Nice");
        flightReq2.setDate("12-10-2017");

        fakeReqHotel.setDate("toto");
        fakeReqCar.setDate("tata");
        fakeReqFlight.setDate("titi");

        flight = new Flight();
        hotel = new Hotel();
        car = new Car();

        flight.setPrice("222");
        flight.setDate("21-10-2017");
        flight.setDestination("Paris");

        car.setDate("21-10-2017");
        car.setDestination("Paris");
        car.setName("toto");
        car.setPrice("300");

        hotel.setDestination("Paris");
        hotel.setName("tata");
        hotel.setPrice("200");


    }

    @Override
    public String isMockEndpointsAndSkip(){
        return HOTELSERVICE_ENDPOINTA +
                "|" + HOTELSERVICE_ENDPOINTB +
                "|" + CARSERVICE_ENDPOINTA +
                "|" + CARSERVICE_ENDPOINTB +
                "|" + FLIGHTSERVICE_ENDPOINTA +
                "|" + FLIGHTSERVICE_ENDPOINTB
                ;
    }

    @Override
    public String isMockEndpoints(){
        return AGGREG_CAR +
                "|" + AGGREG_FLIGHT +
                "|" + AGGREG_HOTEL +
                "|" + AGGREG_TRAVELREQUEST +
                "|" + RETRIEVE_A_HOTELA +
                "|" + RETRIEVE_A_HOTELB +
                "|" + RETRIEVE_A_FLIGHTA +
                "|" + RETRIEVE_A_FLIGHTB +
                "|" + RETRIEVE_CAR_A +
                "|" + RETRIEVE_CAR_B +
                "|" + DEATH_POOL
                ;
    }

    @Before
    public void initMocks(){
        resetMocks();

        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");

        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "POST");

        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "POST");

    }

    @Test
    public void testExecutionContext() throws Exception{
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTA);
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_A_HOTELA);
        isAvailableAndMocked(RETRIEVE_A_HOTELB);
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTA);
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTA);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTB);
        isAvailableAndMocked(CARSERVICE_ENDPOINTA);
        isAvailableAndMocked(CARSERVICE_ENDPOINTB);
        isAvailableAndMocked(RETRIEVE_CAR_A);
        isAvailableAndMocked(RETRIEVE_CAR_B);
        isAvailableAndMocked(AGGREG_CAR);
        isAvailableAndMocked(AGGREG_FLIGHT);
        isAvailableAndMocked(AGGREG_HOTEL);
        isAvailableAndMocked(AGGREG_TRAVELREQUEST);
        isAvailableAndMocked(DEATH_POOL);

    }

    @Test
    public void testNotFoundFromBothFlightServices() throws Exception{
        mock(FLIGHTSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "toto";
            exc.getIn().setBody(req);
        });
        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "toto";
            exc.getIn().setBody(req);
        });
        mockCarA();
        mockCarB();
        mockHotelA();
        mockHotelB();

        mock(DEATH_POOL).expectedMessageCount(1);

        template.sendBody(RETRIEVE_A_FLIGHTA,flightReq);
        template.sendBody(RETRIEVE_A_FLIGHTB,flightReq);

        Flight expectedFlightA = new Flight();
        Flight responseFromA = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();
        expectedFlightA.setDestination("not found");
        expectedFlightA.setDate("not found");

        Flight expectedFlightB = new Flight();
        Flight responseFromB = (Flight) mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();
        expectedFlightB.setDestination("not found");
        expectedFlightB.setDate("not found");

        assertEquals(expectedFlightA.getDestination(), responseFromA.getDestination());
        assertEquals(expectedFlightA.getDate(), responseFromA.getDate());

        assertEquals(expectedFlightB.getDestination(), responseFromB.getDestination());
        assertEquals(expectedFlightB.getDate(), responseFromB.getDate());

        Map<String, Object> headers = new HashMap<>();
        headers.put("type","flight");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, responseFromA,headers);
        headers.clear();
        headers.put("type","car");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, car, headers);

        headers.clear();
        headers.put("type","hotel");
        template.sendBodyAndHeaders(AGGREG_TRAVELREQUEST, hotel, headers);

        mock(DEATH_POOL).assertIsSatisfied();
        assertEquals(mock(DEATH_POOL).getReceivedExchanges().get(0).getIn().getHeader("err"),"flightnotfound");

    }

    public void mockFlyA(){
        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange e) -> {
            String res = "{\n" +
                    "  \"size\": 3,\n" +
                    "  \"vols\": [\n" +
                    "    {\n" +
                    "      \"date\": \"2017-10-12\",\n" +
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
                    "      \"date\": \"2017-10-12\",\n" +
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
                    "      \"date\": \"2017-10-12\",\n" +
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
            e.getIn().setBody(res);
        });

    }
    public void mockFlyB(){
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
    }
    public void mockCarA(){
        mock(CARSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "    \"date\": \"28/11/2017\",\n" +
                    "    \"price\": 30,\n" +
                    "    \"name\": \"Car1\",\n" +
                    "    \"destination\": \"Lyon\"\n" +
                    "  }]";
            exc.getIn().setBody(req);
        });
    }
    public void mockCarB(){
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

    }
    public void mockHotelA(){
        mock(HOTELSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "  \"date\": \"28/11/2017\",\n" +
                    "  \"price\": 30,\n" +
                    "  \"name\": \"Hotel4\",\n" +
                    "  \"destination\": \"Paris\"\n" +
                    "}]";
            exc.getIn().setBody(req);
        });
    }
    public void mockHotelB(){
        mock(HOTELSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "[{\n" +
                    "  \"city\": \"Ipaba\",\n" +
                    "  \"name\": \"Lockman and Sons\",\n" +
                    "  \"roomCost\": 51\n" +
                    "}]";
            exc.getIn().setBody(req);
        });
    }





}
