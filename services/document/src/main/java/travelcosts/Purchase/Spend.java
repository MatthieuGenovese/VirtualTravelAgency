package travelcosts.Purchase;

import org.json.JSONObject;
import travelcosts.date.Date;

public class Spend {
    private String id;
    private double prix;
    private Date date;
    private String reason;

    public Spend(String id,double prix,String reason,Date date){
        this.id = id;
        this.prix = prix;
        this.reason = reason;
        this.date = date;
    }

    public Spend(JSONObject data){
        System.out.println("Spend: "+data);
        this.id = data.getString("id");
        this.prix = data.getDouble("prix");
        this.reason = data.getString("reason");
        this.date = new Date(data.getString("date"));
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("prix", prix)
                .put("reason",reason)
                .put("date",date);
    }


    @Override
    public String toString() {
        return "Spend{" + "id='" + id + '\'' + ", reason=" + reason + '\'' + ", prix=" + prix + '\'' + ", date=" + date+ '}';
    }


    public String getId() {
        return id;
    }

    public double getPrix() {
        return prix;
    }

    public String getReason() {
        return reason;
    }

    public Date getDate() {
        return date;
    }
}
