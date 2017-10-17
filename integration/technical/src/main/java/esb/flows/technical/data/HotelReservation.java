/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 *
 * @author lm203441
 */
public class HotelReservation implements Serializable{

    @JsonProperty private String date;
    @JsonProperty private String destination;
    
    public HotelReservation() {
    }
    
    public HotelReservation(String date, String destination) {
       this.date = date;
       this.destination = destination;
    }


    public String getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    
    
//    JSONObject toJson() {
//        return new JSONObject()
//                .put("name", name)
//                .put("date", date)
//                .put("destination", destination)
//                .put("price", price);
//    }

    @Override
    public String toString() {
        return "HotelReservation{" + "date=" + date + ", destination=" + destination + '}';
    }
    
    
}
