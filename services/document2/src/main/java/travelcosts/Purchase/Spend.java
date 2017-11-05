package travelcosts.Purchase;

import org.json.JSONObject;

public class Spend {
    private String id;
    private String country;
    private String date;
    private String reason;
    private Price price;

    public Spend(String id,String country,String date,String reason,Price price){
        this.id = id;
        this.country = country;
        this.price = price;
        this.reason = reason;
        this.date = date;
    }

    public Spend(JSONObject data){
        this.id = data.getString("id");
        this.country = data.getString("country");
        this.reason = data.getString("reason");
        this.date = data.getString("date");
        this.price = new Price(data.getJSONObject("price"));
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("country", country)
                .put("prix", this.price.toJson())
                .put("reason",reason)
                .put("date",date);
    }

    public Price getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

}
