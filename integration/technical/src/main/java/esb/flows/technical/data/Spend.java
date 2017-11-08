package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Spend implements Serializable{
      /*{ "type":"submit", "spends":
        { "id":22, "identity":
            { "firstName":"mohamed", "lastName":"chennouf", "email":"mohamed.chennouf@etu.unice" },
            "spends": [ { "id":"0", "reason":"Restaurant", "date":"05/02/2018", "country": "AM" , "price" :
                            { "price":120, "currency":"EUR" } },
                        { "id":"1", "reason":"Restaurant", "date":"01/02/2005", "country": "AM" , "price" :
                            { "price":90, "currency":"EUR" } } ] } }*/

    @JsonProperty private String id;
    @JsonProperty private String reason;
    @JsonProperty private String date;
    @JsonProperty private String country;
    @JsonProperty private Price prix;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Price getPrice() {
        return prix;
    }

    public void setPrice(Price prix) {
        this.prix = prix;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Spend{" +
                "id='" + id + '\'' +
                ", reason='" + reason + '\'' +
                ", date='" + date + '\'' +
                ", country='" + country + '\'' +
                ", price=" + prix +
                '}';
    }
}
