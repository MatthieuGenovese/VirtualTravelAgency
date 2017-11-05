package travelcosts.Purchase;

public enum Status {

    EN_ATTENTE("EN_ATTENTE"),
    VALIDE("VALIDE"),
    REJECTED("REJECTED");

    String str;

    Status(String str){
        this.str = str;
    }

    public String getStr(){
        return str;
    }

}
