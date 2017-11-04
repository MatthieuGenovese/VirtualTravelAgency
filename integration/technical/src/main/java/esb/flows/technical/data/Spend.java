package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Spend implements Serializable{

    @JsonProperty private String id;
    @JsonProperty private String prix;
    @JsonProperty private String reason;
    @JsonProperty private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
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
    /*{ "type":"submit", "spends":
        { "id":22, "identity":
            { "firstName":"kaka", "lastName":"susuususu", "email":"popopo@etu.unice" },
            "spend": { "id":"strzing", "prix":23, "reason":"strinzg", "date":"staaring" }

        }
*/
}
