package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SpendRequest implements Serializable {

    @JsonProperty private String id;
    @JsonProperty private Identity identity;
    @JsonProperty private Spend spend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Spend getSpend() {
        return spend;
    }

    public void setSpend(Spend spend) {
        this.spend = spend;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

}
