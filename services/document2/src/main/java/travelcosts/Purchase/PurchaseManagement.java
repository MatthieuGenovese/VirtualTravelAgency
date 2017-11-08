package travelcosts.Purchase;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import travelcosts.seuil.Seuil;

import java.util.Iterator;
import java.util.List;

public class PurchaseManagement {
    private Status status;
    private int id;
    private Identity identity;
    private Spend[] spends;
    private double totalSeuil= 0;
    private double totalSpends = 0;
    private String justification;

    @MongoObjectId
    String _id;

    public PurchaseManagement(JSONObject spend) throws Exception {
        this.id = spend.getInt("id");
        this.identity = new Identity(spend.getJSONObject("identity"));
        Seuil seuil = new Seuil();
        JSONArray values = spend.getJSONArray("spends");//new Spend[]{new Spend(spend.getJSONObject("spends")), "toto"};
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

    public PurchaseManagement() {}

    public PurchaseManagement(int id,Status status,Identity identity,Spend[] spends,double totalSeuil, double totalSpends,String justification){
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


}