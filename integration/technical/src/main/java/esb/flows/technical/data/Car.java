package esb.flows.technical.data;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Jeremy on 22/10/2017.
 */
public class Car implements Serializable, ItemInterface {

    @JsonProperty("destination") private String destination;
    @JsonProperty("date") private String date;
    @JsonProperty("price") private String price;
    @JsonProperty("name") private String name;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
