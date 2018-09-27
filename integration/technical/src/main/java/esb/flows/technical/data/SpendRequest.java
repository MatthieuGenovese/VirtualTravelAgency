package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpendRequest implements Serializable {

    @JsonProperty private String type;

    @JsonProperty private Bills bills;

    @Override
    public String toString() {
        return "SpendRequest{" +
                "type='" + type + '\'' +
                ", bills=" + bills +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bills getBills() {
        return bills;
    }

    public void setBills(Bills bills) {
        this.bills = bills;
    }



}
