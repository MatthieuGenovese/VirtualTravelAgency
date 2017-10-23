package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable, ItemInterface {

    @JsonProperty("name") private String name;

    @JsonProperty("destination") private String destination;

//    @JsonProperty("date") private String date;

    @JsonProperty("price") private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Hotel{" + "name=" + name + ", destination=" + destination + ", price=" + price + '}';
    }


}
