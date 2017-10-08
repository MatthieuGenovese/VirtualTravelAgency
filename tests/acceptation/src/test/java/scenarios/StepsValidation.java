package scenarios;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;
import submittravel.data.tickets.Carticket;
import submittravel.data.tickets.Hostelticket;
import submittravel.data.tickets.Planeticket;
import submittravel.service.TravelDecisionImpl;

import static org.junit.Assert.*;

public class StepsValidation {

    TravelDecisionImpl service;
    Carticket ct;
    Hostelticket ht;
    Planeticket pt;
    TravelRequest tr;

    @Given("^my validation service$")
    public void myValidationService() throws Throwable {
        service = new TravelDecisionImpl();
    }

    @And("^i have choose all my tickets$")
    public void iHaveChooseAllMyTickets() throws Throwable {
        pt = new Planeticket();
        ht = new Hostelticket();
        ct = new Carticket();
    }

    @And("^i send them to my superior$")
    public void iSendThemToMySuperior() throws Throwable {
        tr = new TravelRequest();
        tr.setIdemploye("ID01");
        tr.setCarticket(ct);
        tr.setHostelticket(ht);
        tr.setPlaneticket(pt);
    }
    
}
