package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpendRequest implements Serializable {

    @JsonProperty private String id;
    @JsonProperty private Identity identity;
    @JsonProperty private List<Spend> spends;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<Spend> getSpend() {
        return spends;
    }

    public void setSpend(List<Spend> spends) {
        this.spends = spends;
    }

    @Override
    public String toString() {
        return "SpendRequest{" +
                "id='" + id + '\'' +
                ", identity=" + identity +
                ", spends=" + spends +
                '}';
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

}
