/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservation;

import java.util.ArrayList;
import java.util.List;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Matthieu on 26/09/2017.
 */
public class FlightReservation {
    
    private String id;

    private String destination;

    private String date;

    private boolean isDirect;

    private String price;

    private ArrayList<String> stops;
    
    @MongoObjectId
    String _id;

    public FlightReservation(String id, String destination, String date, boolean isDirect, ArrayList<String> stops, String price){
        this.id = id;
        this.date = date;
        this.destination = destination;
        this.isDirect = isDirect;
        this.stops = stops;
        this.price = price;
    }

//    public String getID(){
//        return id;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsDirect() {
        return isDirect;
    }

    public void setIsDirect(boolean isDirect) {
        this.isDirect = isDirect;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public ArrayList<String> getStops() {
        return stops;
    }

    public String getPrice(){
        return price;
    }

    public String toString(){
        if(!isDirect){
            String tmp = "Escales : ";
            for(String s : stops){
                tmp += " " + s;
            }
            return "Destination : " + getDestination() + " Date : " + getDate() + " " + tmp + " " + getPrice();
        }
        return "Destination : " + getDestination() + " Date : " + getDate() + " Vol direct " + getPrice();
    }
    
    public FlightReservation() {}
    
    public FlightReservation(JSONObject data) {
        this.id = data.getString("id");
        this.destination = data.getString("destination");
        this.date = data.getString("date");
        this.price = data.getString("price");
        this.isDirect = data.getBoolean("isDirect");
        JSONArray stops = data.getJSONArray("stops");
        List<String> stoplist = new ArrayList<String>();
        for(int i=0;i<stops.length();i++){
            stoplist.add(stops.getString(i));
        }
        this.stops = (ArrayList<String>) stoplist;
    }
    
    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("destination", destination)
                .put("date", date)
                .put("price", price)
                .put("isDirect", isDirect)
                .put("stops", stops);
    }

}
