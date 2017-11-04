package travelcosts.Purchase;

import org.json.JSONObject;

public class Price {
    private double prix;
    private String currency;


    public Price(JSONObject data){
        System.out.println("Price: "+data);
        this.prix = data.getDouble("price");
        this.currency = data.getString("currency");
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("price", prix)
                .put("currency",currency);
    }


    @Override
    public String toString() {
        return "{ price=" + prix + '\'' + ", currency=" + currency+'}';
    }

}
