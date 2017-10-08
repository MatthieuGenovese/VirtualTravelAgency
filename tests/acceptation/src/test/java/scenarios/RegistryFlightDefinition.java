
package scenarios;

import cucumber.api.java.en.*;
import org.json.JSONObject;
import java.util.Collections;
import org.json.JSONArray;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.WebClient;
import static org.junit.Assert.*;

public class RegistryFlightDefinition {


    private String host = "host";
    private int port = 8080;

    private JSONObject flight;
    private JSONObject answer;
    private String id;
    private String filter;
    private String safeWord;

    private JSONObject call(JSONObject request) {
        String raw =
                WebClient.create("http://" + host + ":" + port + "/flightreservation-service-document/registry")
                        .accept(MediaType.APPLICATION_JSON_TYPE)
                        .header("Content-Type", MediaType.APPLICATION_JSON)
                        .post(request.toString(), String.class);
        return new JSONObject(raw);
    }

    @Given("^an empty registry deployed on (.*):(\\d+)$")
    public void set_clean_registry(String host, int port) {
        this.host = host;
        this.port = port;
        JSONObject ans = call(new JSONObject().put("event", "PURGE").put("use_with", "caution"));
        assertEquals("done", ans.getString("purge"));
    }

    @Given("^a flight with id (.*) added to the registry$")
    public void upload_preregistered_flight(String id) {
        JSONObject flight = new JSONObject();
            flight.put("id", id)
                    .put("destination", "Paris")
                    .put("price", "200")
                    .put("date", "2017-10-10")
                    .put("isDirect", "true")
                    .put("stops", new JSONArray());
        JSONObject ans = call(new JSONObject().put("event", "REGISTER").put("flightreservation", flight));
        assertEquals(true, ans.getBoolean("inserted"));
    }


    @Given("^A flight identified as (.*)$")
    public void initialize_a_flight(String identifier) { flight = new JSONObject(); flight.put("id", identifier); }

    @Given("^with a string (.*) set to (.*)$")
    public void add_flight_attribute_string(String key, String value) {
        flight.put(key.trim(),value);  }
    
    @Given("^with an array (.*) set to (.*)$")
    public void add_flight_attribute_array(String key, JSONArray value) {
        flight.put(key.trim(),value);  }
    
    @Given("^with a boolean (.*) set to (.*)$")
    public void add_flight_attribute_boolean(String key, boolean value) {
        flight.put(key.trim(),value);  }

    @Given("^an id identified as (.*)$")
    public void perso_of_interest_id(String id) { this.id = id; }

    @Given("^the (.*) safe word$")
    public void setting_safe_word(String word) { this.safeWord = word; }

    @Given("^a filter set to \"(.*)\"$")
    public void setting_filter(String filter) { this.filter = filter; }

    @When("^the (.*) message is sent$")
    public void call_registry(String message) {
        JSONObject request = new JSONObject();
        switch(message) {
            case "REGISTER":
                request.put("event", message).put("flightreservation", flight);
                break;
            case "RETRIEVE":
                request.put("event", message).put("id", id); break;
            case "DELETE":
                request.put("event", message).put("id", id); break;
            case "LIST":
                request.put("event", message).put("filter", filter); break;
            case "DUMP":
                request.put("event", message); break;
            case "PURGE":
                request.put("event", message).put("use_with", safeWord); break;
            default:
                throw new RuntimeException("Unknown message");
        }
        answer = call(request);
        assertNotNull(answer);
    }

    @Then("^the flight is registered$")
    public void the_flight_is_registered() {
        assertEquals(true,answer.getBoolean("inserted"));
    }

    @Then("^the flight is removed$")
    public void the_flight_is_deleted() {
        assertEquals(true,answer.getBoolean("deleted"));
    }

    @Then("^the (.*) is equals to (.*)$")
    public void check_flight_content(String key, String value) {
        Object data = answer.getJSONObject("flightreservation").get(key.trim());
        if(data.getClass().equals(Integer.class)) {
            assertEquals(Integer.parseInt(value.trim()), data);
        } 
        else if(data.getClass().equals(JSONArray.class)){
            assertEquals(value, data.toString());
        } 
        else if (data.getClass().equals(Boolean.class)){
            assertEquals(Boolean.parseBoolean(value), data);
        }
        else{
            assertEquals(value.trim(), data);
        }
    }

    @Then("^there (?:are|is) (\\d+) flight(?:s)? in the registry$")
    public void how_many_flights_in_the_registry(int expected) {
        JSONObject res = call(new JSONObject().put("event", "DUMP"));
        assertEquals(expected,res.getInt("size"));
    }

    @Then("^the flight exists$")
    public void the_flight_exists() {
        JSONObject flight = new JSONObject(answer.toString());
        answer = new JSONObject().put("flightreservation", flight);
    }

    @Then("^the answer contains (\\d+) result(?:s)?$")
    public void the_size_is_good(int expected) {
        assertEquals(expected, answer.getInt("size"));
    }

}
