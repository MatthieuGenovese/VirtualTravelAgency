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
}
