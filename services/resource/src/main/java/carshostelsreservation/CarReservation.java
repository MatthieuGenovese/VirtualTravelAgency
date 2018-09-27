package carshostelsreservation;

import org.json.JSONObject;

public class CarReservation {
    private String name;

    private Integer price;

    private String date;

    private String destination;

    public CarReservation(String name, Integer price, String date, String destination) {
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
