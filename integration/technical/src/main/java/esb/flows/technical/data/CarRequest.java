package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jeremy on 22/10/2017.
 */
public class CarRequest implements Serializable {

    @JsonProperty("destination") private String destination;
    @JsonProperty("date") private String date;
    @JsonProperty("end") private String end;
    @JsonProperty("sort") private String sort;

    public CarRequest(){}

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
