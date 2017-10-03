/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flyreservation;

import java.util.ArrayList;
import java.util.List;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Matthieu on 26/09/2017.
 */
public class FlyReservation {

    private String destination;

    private String date;

    private boolean isDirect;

    private double price;

    private ArrayList<String> stops;
    
    @MongoObjectId
    String _id;

    public FlyReservation(String destination, String date, boolean isDirect, ArrayList<String> stops, double price){
        this.date = date;
        this.destination = destination;
        this.isDirect = isDirect;
        this.stops = stops;
        this.price = price;
    }

    public String getID(){
        return destination + date + Double.toString(price);
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

    public double getPrice(){
        return price;
    }

    public String toString(){
        if(!isDirect){
            String tmp = "Escales : ";
            for(String s : stops){
                tmp += " " + s;
            }
            return "Destination : " + getDestination() + " Date : " + getDate() + " " + tmp + " " + Double.toString(getPrice());
        }
        return "Destination : " + getDestination() + " Date : " + getDate() + " Vol direct " + Double.toString(getPrice());
    }
    
    public FlyReservation() {}
    
    public FlyReservation(JSONObject data) {
        this.destination = data.getString("destination");
        this.date = data.getString("date");
        this.price = data.getInt("price");
        this.isDirect = data.getBoolean("isDirect");
        JSONArray stops = data.getJSONArray("stops");
        List<String> stoplist = new ArrayList<String>();
        for(int i=0;i<stops.length();i++){
            stoplist.add(stops.getJSONObject(i).getString("stop"));
        }
        this.stops = (ArrayList<String>) stoplist;
    }
    
    JSONObject toJson() {
        return new JSONObject()
                .put("destination", destination)
                .put("date", date)
                .put("price", price)
                .put("isDirect", isDirect)
                .put("stops", stops);
    }

}
