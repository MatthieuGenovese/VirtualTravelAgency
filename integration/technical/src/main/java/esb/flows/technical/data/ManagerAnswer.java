package esb.flows.technical.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagerAnswer {
    @JsonProperty  private String reponse;
    @JsonProperty  private String reponseService;

    public ManagerAnswer(){

    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getReponseService() {
        return reponseService;
    }

    public void setReponseService(String reponseService) {
        this.reponseService = reponseService;
    }
}
