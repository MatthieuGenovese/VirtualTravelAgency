package esb.flows;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import java.io.StringReader;

import static esb.flows.technical.utils.Endpoints.*;

public class SendRequestToManagerTest extends ActiveMQTest {
    private Flight f;
    private Car c;
    private Hotel h;
    private TravelAgencyRequest tr;
    private String request;
    private String repEndpoint = "<ns2:answerResponse xmlns:ns2=\"http://informatique.polytech.unice.fr/soa1/cookbook/\"><result><answer>Submit : 0: \n" +
            "CarTicket{\n" +
            "  start date:2017-04-26T06:45:54Z  end date:2017-04-26T06:45:54Z  model:Peugeot  price:20}\n" +
            "HostelTicket{\n" +
            " address:Ipaba  start date:0 end_date:21/01/1203  price:51}\n" +
            "PlaneTicket{\n" +
            "  departure date:12-10-2017  departure place:0  destination place:Paris  seat:0  price:20}\n" +
            "</answer><identifier>Id : 0</identifier></result></ns2:answerResponse>";

    private String repAttendue = "Submit : 0: \n" +
            "CarTicket{\n" +
            "  start date:2017-04-26T06:45:54Z  end date:2017-04-26T06:45:54Z  model:Peugeot  price:20}\n" +
            "HostelTicket{\n" +
            " address:Ipaba  start date:0 end_date:21/01/1203  price:51}\n" +
            "PlaneTicket{\n" +
            "  departure date:12-10-2017  departure place:0  destination place:Paris  seat:0  price:20}\n" +
            "Id : 0";

    //on definie ici les endpoint non testé (on mettra leurs réponses en durs)
    @Override
    public String isMockEndpointsAndSkip() {
        return MANAGER_REQUEST_ENDPOINT
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return REQUETE_QUEUE + "|" + DEATH_POOL
                ;
    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(MANAGER_REQUEST_ENDPOINT);
        isAvailableAndMocked(REQUETE_QUEUE);
        isAvailableAndMocked(DEATH_POOL);
    }


    //on initialise les requetes de tests
    @Before
    public void initRequests(){
        f = new Flight();
        f.setDestination("Paris");
        f.setDate("12-10-2017");
        f.setPrice("20");

        c = new Car();
        c.setPrice("20");
        c.setDate("2017-04-26T06:45:54Z");
        c.setDestination("bibou");
        c.setName("bibou");

        h = new Hotel();
        h.setPrice("51");
        h.setName("bibou");
        h.setDestination("Ipaba");

        tr = new TravelAgencyRequest();
        tr.setHotelReq(h);
        tr.setFlightReq(f);
        tr.setCarReq(c);

        StringBuilder builder = new StringBuilder();
        builder.append("<cook:answer xmlns:cook=\"http://informatique.polytech.unice.fr/soa1/cookbook/\">\n");
        builder.append("  <request>\n");
        builder.append("    <address_hostel>" + h.getDestination() + "</address_hostel>\n");
        builder.append("    <departure_date_plane>" + f.getDate() + "</departure_date_plane>\n");
        builder.append("    <departure_place_plane>" + "0" + "</departure_place_plane>\n");
        builder.append("    <destination_place_plane>" + f.getDestination() + "</destination_place_plane>\n");
        builder.append("    <end_date_car>"+ c.getDate() + "</end_date_car>\n");
        builder.append("    <end_date_hostel>" + "21/01/1203" + "</end_date_hostel>\n");
        builder.append("    <id>" + "0" + "</id>\n");
        builder.append("    <model_car>"+ "Peugeot" + "</model_car>\n");
        builder.append("    <price_car>" + c.getPrice() +"</price_car>\n");
        builder.append("    <price_hostel>" + h.getPrice() + "</price_hostel>\n");
        builder.append("    <price_plane>"+ f.getPrice() + "</price_plane>\n");
        builder.append("    <seat_plane>" + "0" + "</seat_plane>\n");
        builder.append("    <start_date_car>" + c.getDate() + "</start_date_car>\n");
        builder.append("    <start_date_hostel>" + "0" + "</start_date_hostel>\n");
        builder.append("  </request>\n");
        builder.append("</cook:answer>\n");
        request = builder.toString();
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();
        //chaque fois que ce service recevra un echange,  il retournera cette réponse

        mock(MANAGER_REQUEST_ENDPOINT).whenAnyExchangeReceived((Exchange exc) -> {
            exc.getIn().setBody(repEndpoint);
        });


        //config car service B
        mock(MANAGER_REQUEST_ENDPOINT).expectedMessageCount(1);
        mock(MANAGER_REQUEST_ENDPOINT).expectedHeaderReceived("Content-Type", "application/json");
        mock(MANAGER_REQUEST_ENDPOINT).expectedHeaderReceived("Accept", "application/json");
        mock(MANAGER_REQUEST_ENDPOINT).expectedHeaderReceived("CamelHttpMethod", "POST");
    }

    @Test
    public void testSendRequestToManager() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(0);
        mock(REQUETE_QUEUE).expectedMessageCount(2);
        template.sendBody(REQUETE_QUEUE, tr);

        mock(MANAGER_REQUEST_ENDPOINT).assertIsSatisfied();

        String requeteSend =  mock(MANAGER_REQUEST_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);

        assertEquals(requeteSend, request);

        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource input = new InputSource(new StringReader(repEndpoint));
        String reponseStr = xpath.evaluate("//result", input);
        assertEquals(repAttendue, reponseStr);
        
        mock(DEATH_POOL).assertIsSatisfied();
    }

}
