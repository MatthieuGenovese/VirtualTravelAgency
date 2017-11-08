package travelcosts.Purchase;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import travelcosts.seuil.Seuil;

import java.util.Iterator;
import java.util.List;

public class BillManagement {
    private Status status;
    private int id;
    private Identity identity;
    private Spend[] spends;
    private double totalSeuil= 0;
    private double totalSpends = 0;
    private String justification;
    private Seuil seuil;

    @MongoObjectId
    public String _id;

    public BillManagement(JSONObject spend) throws Exception {
        this.id = spend.getInt("id");
        this.identity = new Identity(spend.getJSONObject("identity"));
        Seuil seuil = new Seuil();
        JSONArray values = spend.getJSONArray("spends");
        spends = new Spend[values.length()];
        for (int i = 0; i < values.length(); i++) {
            Spend s = new Spend(values.getJSONObject(i));
            spends[i] = s;
            this.totalSeuil += seuil.calculateSeuil(s.getCountry(),s.getDate(),s.getPrice().getCurrency());
        }
        if(getTotalSpends()<totalSeuil){
            this.status = Status.VALIDE;
        }else{
            this.status = Status.EN_ATTENTE;
        }
        this.totalSpends = getTotalSpends();
        this.justification = "";
    }

    public BillManagement() {}

    public BillManagement(int id, Status status, Identity identity, Spend[] spends, double totalSeuil, double totalSpends, String justification){
        this.id = id;
        this.status = status;
        this.identity = identity;
        this.spends = spends;
        this.totalSeuil = totalSeuil;
        this.totalSpends = totalSpends;
        this.justification = justification;
    }



        public JSONObject toJson(){
        JSONObject myJson = new JSONObject();
        myJson.put("status", status.getStr())
                    .put("id", id)
                    .put("identity", identity.toJson())
                    .put("spends", spends)
                    .put("totalSeuil",totalSeuil)
                    .put("totalSpends",totalSpends)
                    .put("justification",justification);
            return  myJson;
        }



       public double getTotalSpends(){
            double price = 0;
            for (int i = 0; i < spends.length;i++){
                price += spends[i].getPrice().getPrice();
            }
            return price;
        }


    public Spend[] getSpends() {
        return spends;
    }

    public void setSpends(Spend[] spends) {
        this.spends = spends;
    }

    public Seuil getSeuil() {
        return seuil;
    }

    public void setSeuil(Seuil seuil) {
        this.seuil = seuil;
    }

    public double getTotalSeuil() {
        return totalSeuil;
    }

    public void setTotalSeuil(double totalSeuil) {
        this.totalSeuil = totalSeuil;
    }

    public void setTotalSpends(double totalSpends) {
        this.totalSpends = totalSpends;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}