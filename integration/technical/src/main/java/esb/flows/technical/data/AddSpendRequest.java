package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddSpendRequest {
    @JsonProperty private String type;
    @JsonProperty private String id;
    @JsonProperty private Spend spends;

    @Override
    public String toString() {
        return "AddSpendRequest{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", spends=" + spends +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Spend getSpend() {
        return spends;
    }

    public void setSpend(Spend spends) {
        this.spends = spends;
    }
}
