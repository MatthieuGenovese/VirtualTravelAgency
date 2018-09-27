package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Justification implements Serializable {
    @JsonProperty private String type;
    @JsonProperty private String id;
    @JsonProperty private String justification;

    @Override
    public String toString() {
        return "Justification{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", justification='" + justification + '\'' +
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

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}
