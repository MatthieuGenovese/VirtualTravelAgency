package travelcosts.BillManagement;

public enum Status {

    EN_ATTENTE("EN_ATTENTE"),
    VALIDE("VALIDE"),
    REJECTED("REJECTED");

    String string;

    Status(String str){
        this.string = str;
    }

    public String getString(){
        return string;
    }

}
