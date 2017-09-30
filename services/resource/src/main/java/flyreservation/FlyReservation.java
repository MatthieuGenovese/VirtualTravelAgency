package flyreservation;

import java.util.ArrayList;

/**
 * Created by Matthieu on 26/09/2017.
 */
public class FlyReservation {

    private String destination;

    private String date;

    private boolean isDirect;

    private double price;

    private ArrayList<String> stops;

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

}
