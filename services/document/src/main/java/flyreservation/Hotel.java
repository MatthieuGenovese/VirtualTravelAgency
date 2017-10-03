/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyreservation;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONObject;
/**
 *
 * @author lm203441
 */
public class Hotel {
    
    private String name;
    
    private Integer price;
    
    private String date;
    
    private String destination;
    
    @MongoObjectId
    String _id;
    
//    public Hotel(String name, Integer price, String date, String destination) {
//        this.name = name;
//        this.price = price;
//        this.date = date;
//        this.destination = destination;
//    }
    
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
    
    public Hotel() {}
    
    public Hotel(JSONObject data) {
        this.name = data.getString("name");
        this.date = data.getString("date");
        this.destination = data.getString("destination");
        this.price = data.getInt("price");
    }
    
    JSONObject toJson() {
        return new JSONObject()
                .put("name", name)
                .put("date", date)
                .put("destination", destination)
                .put("price", price);
    }
    
    
}
