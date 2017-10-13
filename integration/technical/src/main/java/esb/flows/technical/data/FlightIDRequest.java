package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightIDRequest {

    @JsonProperty private String event;
    @JsonProperty private Flight citizen;

    public FlightIDRequest(String event, Flight citizen) {
        this.event = event;
        this.citizen = citizen;
    }

    public FlightIDRequest() {}

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public Flight getFlight() { return citizen; }
    public void setFlight(Flight citizen) { this.citizen = citizen; }
}
