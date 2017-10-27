package esb.flows;

import esb.flows.technical.data.CarRequest;
import esb.flows.technical.data.Flight;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.HotelReservation;
import esb.flows.technical.utils.Endpoints;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static esb.flows.technical.utils.Endpoints.*;

public class CallExternalPartnersTest extends ActiveMQTest {
    FlightRequest flightReq;
    HotelReservation hotelReq;
    CarRequest carReq;



    @Override
    public String isMockEndpointsAndSkip() {
        return FLIGHTSERVICE_ENDPOINTA + "|" + FLIGHTSERVICE_ENDPOINTB + "|" + CARSERVICE_ENDPOINTA  + "|" + CARSERVICE_ENDPOINTB  + "|" +
                HOTELSERVICE_ENDPOINTA  + "|" + HOTELSERVICE_ENDPOINTB + "|" + AGGREG_CAR  + "|" + AGGREG_FLIGHT + "|" + AGGREG_HOTEL
                ;
    }

    @Override
    public String isMockEndpoints() {
        return DEATH_POOL +
                "|" + RETRIEVE_A_FLIGHTA +
                "|" + RETRIEVE_A_FLIGHTB +
                "|" + RETRIEVE_CAR_A +
                "|" + RETRIEVE_CAR_B +
                "|" + RETRIEVE_A_HOTELA +
                "|" + RETRIEVE_A_HOTELB
        ;
    }

    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTA);
        isAvailableAndMocked(FLIGHTSERVICE_ENDPOINTB);
        isAvailableAndMocked(CARSERVICE_ENDPOINTA);
        isAvailableAndMocked(CARSERVICE_ENDPOINTB);
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTA);
        isAvailableAndMocked(HOTELSERVICE_ENDPOINTB);
        isAvailableAndMocked(AGGREG_CAR);
        isAvailableAndMocked(AGGREG_FLIGHT);
        isAvailableAndMocked(AGGREG_HOTEL);
        isAvailableAndMocked(RETRIEVE_A_HOTELB);
        isAvailableAndMocked(RETRIEVE_A_HOTELA);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTA);
        isAvailableAndMocked(RETRIEVE_A_FLIGHTB);
        isAvailableAndMocked(RETRIEVE_CAR_A);
        isAvailableAndMocked(RETRIEVE_CAR_B);
        isAvailableAndMocked(DEATH_POOL);
    }


    @Before
    public void initRequests(){
        flightReq = new FlightRequest();
        hotelReq = new HotelReservation();
        carReq = new CarRequest();

        flightReq.setEvent("One_Way_Price");
        flightReq.setIsDirect("false");
        flightReq.setDestination("Paris");
        flightReq.setOrigine("Nice");
        flightReq.setDate("12-10-2017");

        carReq.setDate("28/11/2017");
        carReq.setDestination("Lyon");
        carReq.setEnd(" 60");
        carReq.setSort("asc");

        hotelReq.setDate("28/11/2017");
        hotelReq.setDestination("Ipaba");
    }

    @Before
    public void initMocks() {
        resetMocks();
        System.out.println("TOIOTROERIUTEROPGFJUDKHGFDLGDLKDFHLKDGR");
        mock(FLIGHTSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange e) -> {
            String req = "{\n" +
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
            e.getIn().setBody(req);
        });

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

        //TODO mettre le json retour attendu
        mock(CARSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "";
            exc.getIn().setBody(req);
        });

        //TODO mettre le json retour attendu
        mock(CARSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "";
            exc.getIn().setBody(req);
        });

        //TODO mettre le json retour attendu
        mock(HOTELSERVICE_ENDPOINTA).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "";
            exc.getIn().setBody(req);
        });

        //TODO mettre le json retour attendu
        mock(HOTELSERVICE_ENDPOINTB).whenAnyExchangeReceived((Exchange exc) -> {
            String req = "";
            exc.getIn().setBody(req);
        });

        //config flight service A
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "POST");

        //config flight service B
        mock(FLIGHTSERVICE_ENDPOINTB).expectedMessageCount(1);
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Content-Type", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(FLIGHTSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "POST");

        //config hotel service A
        mock(HOTELSERVICE_ENDPOINTA).expectedMessageCount(1);
        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        //config hotel service B
        mock(HOTELSERVICE_ENDPOINTB).expectedMessageCount(1);
        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(HOTELSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");

        //config car service A
        mock(CARSERVICE_ENDPOINTA).expectedMessageCount(1);
        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTA).expectedHeaderReceived("CamelHttpMethod", "GET");

        //config car service B
        mock(CARSERVICE_ENDPOINTB).expectedMessageCount(1);
        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("Accept", "application/json");
        mock(CARSERVICE_ENDPOINTB).expectedHeaderReceived("CamelHttpMethod", "GET");
    }


    @Test
    public void testRetrive2Flight() throws Exception {

        mock(AGGREG_FLIGHT).expectedMessageCount(2);
        mock(FLIGHTSERVICE_ENDPOINTA).expectedMessageCount(1);
        mock(FLIGHTSERVICE_ENDPOINTB).expectedMessageCount(1);
        mock(DEATH_POOL).expectedMessageCount(0);
        mock(RETRIEVE_A_FLIGHTA).expectedMessageCount(1);
        mock(RETRIEVE_A_FLIGHTB).expectedMessageCount(1);
        // Calling the integration flow
        template.sendBody(RETRIEVE_A_FLIGHTA, flightReq);
        //template.asyncSendBody(RETRIEVE_A_FLIGHTA, flightReq);
        //template.send(RETRIEVE_A_FLIGHTA, (Exchange) flightReq);
        //template.requestBody(RETRIEVE_A_FLIGHTA, flightReq);

        mock(FLIGHTSERVICE_ENDPOINTA).assertIsSatisfied();

        Flight expectedFlightA = new Flight();
        Flight responseFlightA = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(0).getIn().getBody();

        expectedFlightA.setDestination("Paris");
        expectedFlightA.setDate("2017-10-12");
        expectedFlightA.setPrice("300");

        assertEquals(expectedFlightA.getDate(), responseFlightA.getDate());
        assertEquals(expectedFlightA.getDestination(), responseFlightA.getDestination());
        assertEquals(expectedFlightA.getPrice(), responseFlightA.getPrice());

       // template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);
        template.sendBody(RETRIEVE_A_FLIGHTB, flightReq);

        mock(FLIGHTSERVICE_ENDPOINTB).assertIsSatisfied();

        Flight expectedFlightB = new Flight();
        Flight responseFlightB = (Flight)  mock(AGGREG_FLIGHT).getReceivedExchanges().get(1).getIn().getBody();

        expectedFlightB.setDestination("Paris");
        expectedFlightB.setDate("2017-10-12");
        expectedFlightB.setPrice("300");

        assertEquals(expectedFlightB.getDate(), responseFlightB.getDate());
        assertEquals(expectedFlightB.getDestination(), responseFlightB.getDestination());
        assertEquals(expectedFlightB.getPrice(), responseFlightB.getPrice());

        mock(AGGREG_FLIGHT).assertIsSatisfied();
        mock(DEATH_POOL).assertIsSatisfied();
    }

}
