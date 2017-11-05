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
    private double mySeuil = 0;
    private double totalSpends = 0;

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
            this.mySeuil += seuil.calculateSeuil(s.getCountry(),s.getDate(),s.getPrice().getCurrency());
        }
        if(getTotalSpends()<mySeuil){
            this.status = Status.VALIDE;
        }else{
            this.status = Status.EN_ATTENTE;
        }
        this.totalSpends = getTotalSpends();
    }



        public JSONObject toJson(){
            return  new JSONObject()
                .put("status", status.getStr())
                .put("id", id)
                .put("identity", identity.toJson())
                .put("spends", spends)
                .put("totalSeuil",mySeuil)
                .put("totalSpends",totalSpends);
        }



        public double getTotalSpends(){
            double price = 0;
            for (int i = 0; i < spends.length;i++){
                price += spends[i].getPrice().getPrix();
            }
            return price;
        }


}