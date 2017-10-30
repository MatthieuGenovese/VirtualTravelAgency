package travelcosts.Purchase;

public enum Status {

    EN_ATTENTE("EN_ATTENTE"),
    VALIDE("VALIDE"),
    NON_VALIDE("NON_VALIDE");

    String str;

    Status(){}

    Status(String str){
        this.str = str;
    }

    public String getStr(){
        return str;
    }

}
