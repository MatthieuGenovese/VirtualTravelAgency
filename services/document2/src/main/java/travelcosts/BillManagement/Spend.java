package travelcosts.BillManagement;

import org.json.JSONObject;

public class Spend {
    private String id;
    private String country;
    private String date;
    private String reason;
    private Price price;

    public Spend(){}

    public Spend(String id,String country,String date,String reason,Price price){
        this.id = id;
        this.country = country;
        this.date = date;
        this.reason = reason;
        this.price = price;
    }

    public Spend(JSONObject data){
        this.id = data.getString("id");
        this.country = data.getString("country");
        this.date = data.getString("date");
        this.reason = data.getString("reason");
        this.price = new Price(data.getJSONObject("prix"));
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("country", country)
                .put("date",date)
                .put("prix", this.price.toJson())
                .put("reason",reason);
    }



    public Price getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price.setPrice(price);
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", country='" + country + '\'' +
                ", date='" + date + '\'' +
                ", prix='" + price.toString() + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

}
