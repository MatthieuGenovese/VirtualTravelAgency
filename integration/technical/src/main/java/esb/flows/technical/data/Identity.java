package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Identity implements Serializable{
    /*{ "type":"submit", "spends":
        { "id":22, "identity":
            { "firstName":"mohamed", "lastName":"chennouf", "email":"mohamed.chennouf@etu.unice" },
            "spends": [ { "id":"0", "reason":"Restaurant", "date":"05/02/2018", "country": "AM" , "price" :
                            { "price":120, "currency":"EUR" } },
                        { "id":"1", "reason":"Restaurant", "date":"01/02/2005", "country": "AM" , "price" :
                            { "price":90, "currency":"EUR" } } ] } }*/

    @JsonProperty private String firstName;
    @JsonProperty private String lastName;
    @JsonProperty private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
