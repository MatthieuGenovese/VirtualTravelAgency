package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SpendAnswer implements Serializable{
    @JsonProperty private String type;
    @JsonProperty private String id;

    @Override
    public String toString() {
        return "SpendAnswer{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
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
}
