package travelcosts.Purchase;

import org.json.JSONObject;

public class Price {
    private double price;
    private String currency;


    public Price(JSONObject data){
        this.price = data.getDouble("price");
        this.currency = data.getString("currency");
    }

    public Price(){}


    JSONObject toJson() {
        return new JSONObject()
                .put("price", price)
                .put("currency",currency);
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    @Override
    public String toString() {
        return "price{" +
                "price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }

}
