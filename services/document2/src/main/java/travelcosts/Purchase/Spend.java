package travelcosts.Purchase;

import org.json.JSONObject;

public class Spend {
    private String id;
    private String date;
    private String reason;
    private Price price;

    public Spend(String id,double prix,String reason,String date,String currency,Price price){
        this.id = id;
        this.price = price;
        this.reason = reason;
        this.date = date;
    }

    public Spend(JSONObject data){
        System.out.println("Spend: "+data);
        this.id = data.getString("id");
        this.reason = data.getString("reason");
        this.date = data.getString("date");
        this.price = new Price(data.getJSONObject("price"));
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("prix", this.price.toJson())
                .put("reason",reason)
                .put("date",date);
    }


    @Override
    public String toString() {
        return "Spend{" + "id='" + id + '\'' + ", reason=" + reason + '\'' + ", Price=" + price.toString() + '\'' + ", date=" + date+ '\'' +'}';
    }


    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }
}
