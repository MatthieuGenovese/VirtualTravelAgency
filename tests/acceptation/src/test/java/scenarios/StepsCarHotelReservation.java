package scenarios;

import carshostelsreservation.*;
import cucumber.api.PendingException;
import cucumber.api.java.en.*;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.*;

import static org.junit.Assert.*;
import org.json.JSONArray;

import java.io.IOException;


public class StepsCarHotelReservation {

    private String host = "localhost";
    private int port = 8080;
    private String method;
    private CarHotelReservationService service;
    private CarReservation c1;
    private CarReservation c2;
    private CarReservation c3;
    Response r;


    @Given("^my service exists$")
    public void my_service_exists() throws Throwable {
        this.host = host;
        this.port = port;
        service = new CarHotelReservationService();
        c1 = new CarReservation("Car1", 60, "28/11/2017", "Lyon");
        c2 = new CarReservation("Car2", 80, "28/12/2017", "Paris");
        c3 = new CarReservation("Car3", 70, "28/11/2017", "Paris");
    }


    @When("^la methode getCarsWithParam est appele$")
    public void la_methode_getCarsWithParam_est_appele() throws Throwable {
        r = service.getCarsWithParam("","");
    }

    @Then("^le resultat renvoi toutes les voitures$")
    public void le_resultat_renvoi_toutes_les_voitures() {
        JSONArray comp = new JSONArray();
        comp.put(c1.toJson());
        comp.put(c3.toJson());
        comp.put(c2.toJson());
        String s = r.readEntity(String.class);
        assertTrue(s.equals(comp.toString(2)));
    }


    @When("^la methode getCarsWithParam est appele avec Lyon$")
    public void laMethodeGetCarsWithParamEstAppeleAvecLyon() throws Throwable {
        r = service.getCarsWithParam("","Lyon");
    }

    @Then("^le resultat renvoi toutes les voitures de Lyon$")
    public void leResultatRenvoiToutesLesVoituresDeLyon() throws Throwable {
        JSONObject j = c1.toJson();
        String s = r.getEntity().toString();
        assertTrue(s.equals("[" + j.toString(2) + "]"));
    }

    @When("^la methode getCarsWithParam est appele avec Paris le (\\d+)/(\\d+)/(\\d+)$")
    public void laMethodeGetCarsWithParamEstAppeleAvecParisLe(int arg0, int arg1, int arg2) throws Throwable {
        String date = arg0 + "/" + arg1 +"/" + arg2;
        r = service.getCarsWithParam(date,"Paris");
    }

    @Then("^le resultat renvoi les voitures disponible a Paris le (\\d+)/(\\d+)/(\\d+)$")
    public void leResultatRenvoiLesVoituresDisponibleAParisLe(int arg0, int arg1, int arg2) throws Throwable {
        JSONObject j = c3.toJson();
        String s = r.getEntity().toString();
        assertTrue(s.equals("[" + j.toString(2) + "]"));    }
}
