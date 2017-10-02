/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carshostelsreservation;

import org.json.JSONObject;

/**
 *
 * @author lm203441
 */
public class HotelReservation {

    private String name;
    
    private Integer price;
    
    private String date;
    
    private String destination;
    
    public HotelReservation(String name, Integer price, String date, String destination) {
       this.name = name;
       this.price = price;
       this.date = date;
       this.destination = destination;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }
    JSONObject toJson() {
        return new JSONObject()
                .put("name", name)
                .put("date", date)
                .put("destination", destination)
                .put("price", price);
    }
    
    
}
