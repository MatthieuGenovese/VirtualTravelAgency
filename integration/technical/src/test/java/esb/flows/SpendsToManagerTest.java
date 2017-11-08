package esb.flows;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.junit.Before;
import org.junit.Test;

import static esb.flows.technical.utils.Endpoints.*;


public class SpendsToManagerTest extends ActiveMQTest {
    SpendRequest sr;
    private String requestSubmit;
    private String requestAddSpends;
    private String requestAddJustification;
    private String repEndpoint;
    private String submitCsv;
    private String addSpendsCsv;
    private String addJustificationCsv;
    
    //on initialise les requetes de tests
    @Before
    public void initRequests(){
        requestSubmit = 
            "{" +
            "\"type\":\"submit\"," +
                "\"bills\":" +
                "{" +
                    "\"id\":\"1\"," +
                    "\"identity\":" +
                        "{" +
                            "\"firstName\":\"momo\"," +
                            "\"lastName\":\"chennouf\"," +
                            "\"email\":\"mc154254@etu.unice.fr\"" +
                        "}," +
                    "\"spends\":" +
                        "[" +
                            "{" +
                                "\"id\":\"01\"," +
                                "\"reason\":\"resto\"," +
                                "\"date\":\"28/06/2006\"," +
                                "\"country\":\"AT\"," +
                                "\"prix\":" +
                                    "{" +
                                        "\"price\":\"45\"," +
                                        "\"currency\":\"EUR\"" +
                                    "}" +
                                "}," +
                                "{" +
                                "\"id\":\"02\"," +
                                "\"reason\":\"avion\"," +
                                "\"date\":\"28/01/2017\"," +
                                "\"country\":\"AT\"," +
                                "\"prix\":" +
                                "{" +
                                    "\"price\":\"98\"," +
                                    "\"currency\":\"EUR\"" +
                                "}" +
                            "}" +
                        "]" +
                    "}" +
                "}";
        
        requestAddSpends = 
            "{"
                + "\"type\":"
                + "\"addSpend\","
                + "\"id\":\"1\","
                + "\"spends\":{"
                +       "\"id\":\"80\","
                +       "\"reason\":\"Taxi\","
                +       "\"date\":\"05/02/2018\","
                +       "\"country\":\"AM\","
                +       "\"prix\":{"
                +           "\"price\":\"44\","
                +           "\"currency\":\"EUR\""
                +       "}"
                + "}"
            + "}";
        
        requestAddJustification =
                "{" +
                    "\"type\":\"addJustification\"," +
                    "\"id\":\"1\"," +
                    "\"justification\":\"Avion en retard\"" +
                "}";

        repEndpoint = "{\n" +
                "  \"spends\": {\n" +
                "    \"spends\": [\n" +
                "      {\n" +
                "        \"date\": \"05/02/2018\",\n" +
                "        \"country\": \"AM\",\n" +
                "        \"price\": {\n" +
                "          \"prix\": 120,\n" +
                "          \"currency\": \"EUR\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"date\": \"01/02/2005\",\n" +
                "        \"country\": \"AM\",\n" +
                "        \"price\": {\n" +
                "          \"prix\": 90,\n" +
                "          \"currency\": \"EUR\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"totalSpends\": 210,\n" +
                "    \"identity\": {\n" +
                "      \"firstName\": \"mohamed\",\n" +
                "      \"lastName\": \"chennouf\",\n" +
                "      \"email\": \"mc154254@etu.unice.fr\"\n" +
                "    },\n" +
                "    \"totalSeuil\": 432,\n" +
                "    \"id\": 22,\n" +
                "    \"status\": \"VALIDE\"\n" +
                "  },\n" +
                "  \"inserted\": true\n" +
                "}";


        submitCsv = "type,idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency\n" +
        "submit,1,momo,chennouf,mc154254@etu.unice.fr,01;02,45;98,resto;avion,28/06/2006;28/01/2017,AT;AT,EUR;EUR";
        
        addSpendsCsv = "type,idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency,justification\n" +
        "addSpend,1,momo,chennouf,mc154254@etu.unice.fr,80,44,Taxi,05/02/2018,AM,EUR";
        
        addJustificationCsv = "type,idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency,justification\n" +
        "addJustification,1,momo,chennouf,mc154254@etu.unice.fr,80,44,Taxi,05/02/2018,AM,EUR,Avion en retard";
    }

    @Override
    public String isMockEndpointsAndSkip() {
        return SPENDSERVICE_ENDPOINT
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return FILE_INPUT_SPEND + "|" + DEATH_POOL
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();
        //chaque fois que ce service recevra un echange,  il retournera cette réponse

        mock(SPENDSERVICE_ENDPOINT).whenAnyExchangeReceived((Exchange exc) -> {
            exc.getIn().setBody(repEndpoint);
        });


    }
    
    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(DEATH_POOL);
        assertNotNull(context.hasEndpoint(FILE_INPUT_SPEND));
//        isAvailableAndMocked(FILE_INPUT_SPEND);
        isAvailableAndMocked(SPENDSERVICE_ENDPOINT);
    }
    
    @Test
    public void testSendRequestSubmitToManager() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(0);
        mock(FILE_INPUT_SPEND).expectedMessageCount(1);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);
        template.sendBody(FILE_INPUT_SPEND, submitCsv);

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String requeteSend =  mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);

        assertEquals(requestSubmit, requeteSend);
            
        
        mock(DEATH_POOL).assertIsSatisfied();
    }
    
    @Test
    public void testSendRequestAddSpendToManager() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(0);
        mock(FILE_INPUT_SPEND).expectedMessageCount(1);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);
        template.sendBody(FILE_INPUT_SPEND, addSpendsCsv);

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String requeteSend =  mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);

        assertEquals(requestAddSpends, requeteSend);
            
        
        mock(DEATH_POOL).assertIsSatisfied();
    }
    
    @Test
    public void testSendRequestAddJustificationToManager() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(0);
        mock(FILE_INPUT_SPEND).expectedMessageCount(1);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);
        template.sendBody(FILE_INPUT_SPEND, addJustificationCsv);

        mock(SPENDSERVICE_ENDPOINT).assertIsSatisfied();

        String requeteSend =  mock(SPENDSERVICE_ENDPOINT).getReceivedExchanges().get(0).getIn().getBody(String.class);

        assertEquals(requestAddJustification, requeteSend);
            
        
        mock(DEATH_POOL).assertIsSatisfied();
    }
}
