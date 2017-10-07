package scenarios;

import carshostelsreservation.CarHotelReservationService;
import cucumber.api.java.en.*;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;


public class TaxComputationStepDefinition {

    private String host = "localhost";
    private int port = 8080;
    private String method;
    private CarHotelReservationService service;


    @Given("^my service exists$")
    public void my_service_exists() throws Throwable {
        this.host = host;
        this.port = port;
        service = new CarHotelReservationService();
    }


    @When("^la methode getCarsWithParam est appele$")
    public void la_methode_getCarsWithParam_est_appele() throws Throwable {
        Response r = service.getCarsWithParam("","");
        System.out.println(r.getEntity().toString());
    }

    @Then("^le resultat renvoi toutes les voitures$")
    public void le_resultat_renvoi_toutes_les_voitures() {
        assertTrue(true);
    }



}
