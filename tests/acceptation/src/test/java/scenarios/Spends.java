
package scenarios;

import cucumber.api.java.en.*;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.JsonElement;
import gherkin.deps.com.google.gson.JsonObject;
import org.json.*;

import java.util.Arrays;
import java.util.Collections;
import org.json.JSONArray;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParser;
import static org.junit.Assert.*;

public class Spends {


    private String host = "host";
    private int port = 8080;

    private JSONObject answer;
    private int id;

    private JSONObject call(JSONObject request) {
        String raw =
                WebClient.create("http://" + host + ":" + port + "/vtg-spends/spends")
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .post(request.toString(), String.class);
        return new JSONObject(raw);
    }

    @Given("^an empty spends registry deployed on (.*):(\\d+)$")
    public void set_clean_registry(String host, int port) {
        this.host = host;
        this.port = port;
        JSONObject ans = call(new JSONObject().put("type", "purge"));
    }

    @Given("^a spend with id (.*) added to the database$")
    public void upload_preregistered_spends(String id) {
        JSONObject price1 = new JSONObject();
        JSONObject price2 = new JSONObject();
        JSONObject spend1 = new JSONObject();
        JSONObject spend2 = new JSONObject();
        JSONArray spends = new JSONArray();
        JSONObject bills = new JSONObject();
        JSONObject identity = new JSONObject();
        spend1.put("id", "0")
                    .put("reason","Restaurant")
                    .put("date","05/02/2018")
                    .put("country","AM")
                    .put("prix", price1);
            
            spend2.put("id", "1")
                    .put("reason","Restaurant")
                    .put("date","01/02/2005")
                    .put("country","AM")
                    .put("prix", price2);
            
            price1.put("price",120)
                    .put("currency", "EUR");
            
            price2.put("price",90)
                    .put("currency", "EUR");
            
            identity.put("firstName", "mohamed")
                    .put("lastName", "chennouf")
                    .put("email", "mohamed.chennouf@etu.unice");
            
            spends.put(spend1)
                    .put(spend2);
            
            bills.put("id", id)
                    .put("identity", identity)
                    .put("spends",spends);
   
        JSONObject ans = call(new JSONObject().put("type", "submit").put("bills", bills));
        assertEquals(true, ans.getBoolean("inserted"));
    }


    @Given("^A mock spend is created")
    public void initialize_a_spend() { 
        JSONObject price1 = new JSONObject();
        JSONObject price2 = new JSONObject();
        JSONObject spend1 = new JSONObject();
        JSONObject spend2 = new JSONObject();
        JSONArray spends = new JSONArray();
        JSONObject bills = new JSONObject();
        JSONObject identity = new JSONObject();
        
        spend1.put("id", "2")
                    .put("reason","Restaurant")
                    .put("date","05/02/2018")
                    .put("country","AM")
                    .put("prix", price1);
            
            spend2.put("id", "3")
                    .put("reason","Restaurant")
                    .put("date","01/02/2005")
                    .put("country","AM")
                    .put("prix", price2);
            
            price1.put("price",120)
                    .put("currency", "EUR");
            
            price2.put("price",90)
                    .put("currency", "EUR");
            identity.put("firstName", "mohamed")
                    .put("lastName", "chennouf")
                    .put("email", "mohamed.chennouf@etu.unice");
            
            spends.put(spend1)
                    .put(spend2);
            
            bills.put("id", "99")
                    .put("identity", identity)
                    .put("spends",spends);
            
            
        answer = bills;
    }
    

    
    @When("^the (.*) message spends service is sent")
    public void call_registry(String message) {
        JSONObject request = new JSONObject();
        switch(message) {
            case "submit":
                request.put("type", message).put("bills", answer);
                break;
            case "purge":
                request.put("type", message); 
                break;
            case "addSpend":
                JSONObject prix = new JSONObject();
                JSONObject spends = new JSONObject();
                prix.put("price",44)
                        .put("currency","EUR");
                spends.put("id", "80")
                        .put("reason", "Taxi")
                        .put("date","05/02/2018")
                        .put("country","AM")
                        .put("prix", prix);
                request.put("type", message).put("id", id).put("spends",spends); 
                break;
            case "addJustification":
                request.put("type", message).put("id", id).put("justification","avion en retard"); 
                break;
            case "retrieve":
                request.put("type", message).put("id", id); 
                break;
            case "validate":
                request.put("type", message).put("id", id); 
                break;
            case "reject":
                request.put("type", message).put("id", id); 
                break;
            default:
                throw new RuntimeException("Unknown message");
        }
        answer = call(request);
        assertNotNull(answer);
    }
    
    @Then("^the spend is registered$")
    public void the_spend_is_registered() {
        assertEquals(true,answer.getBoolean("inserted"));
    }
    
    @Then("^the request is approved$")
    public void the_request_is_approved() {
        assertEquals(true,answer.getBoolean("approved"));
        assertEquals(id,answer.getInt("id"));
    }
    
    @Then("^the request is not approved$")
    public void the_request_is_not_approved() {
        assertEquals(false,answer.getBoolean("approved"));
        assertEquals(id,answer.getInt("id"));
    }
    
    @Then("^the request is rejected$")
    public void the_request_is_rejected() {
        assertEquals(true,answer.getBoolean("rejected"));
        assertEquals(id,answer.getInt("id"));
    }
    
    @Then("^the request is not rejected$")
    public void the_request_is_not_rejected() {
        assertEquals(false,answer.getBoolean("rejected"));
        assertEquals(id,answer.getInt("id"));
    }
    
    @Given("^check justification of request")
    public void check_justificaiton_result(){
        assertEquals(answer.get("justification"), true);
        assertEquals(id,answer.getInt("id"));
    }
    
    @Given("^an id spend identified as (.*)$")
    public void perso_of_interest_id(int id) { this.id = id; }

    @Given("^check totalSpend of request")
    public void check_total_result(){
        assertEquals(answer.get("totalSpends"), 210);
    }
    
    @Given("^check totalSpend and add of request")
    public void check_total_result_after_add(){
        assertEquals(answer.get("totalSpends"), 254);
    }
}
