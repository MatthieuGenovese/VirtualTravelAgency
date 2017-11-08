package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Bills {

    @JsonProperty private String id;
    @JsonProperty private Identity identity;
    @JsonProperty private List<Spend> spends;

    @Override
    public String toString() {
        return "Bills{" +
                "id='" + id + '\'' +
                ", identity=" + identity +
                ", spends=" + spends +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public List<Spend> getSpends() {
        return spends;
    }

    public void setSpends(List<Spend> spends) {
        this.spends = spends;
    }
}
