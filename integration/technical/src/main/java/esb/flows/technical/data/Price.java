package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Price {
     /*{ "type":"submit", "spends":
        { "id":22, "identity":
            { "firstName":"mohamed", "lastName":"chennouf", "email":"mohamed.chennouf@etu.unice" },
            "spends": [ { "id":"0", "reason":"Restaurant", "date":"05/02/2018", "country": "AM" , "price" :
                            { "price":120, "currency":"EUR" } },
                        { "id":"1", "reason":"Restaurant", "date":"01/02/2005", "country": "AM" , "price" :
                            { "price":90, "currency":"EUR" } } ] } }*/


    @JsonProperty private String price;
    @JsonProperty private String currency;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price='" + price + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
