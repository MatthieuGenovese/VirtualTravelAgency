package travelcosts.Purchase;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class PurchaseManagement {
    private Status status;
    private int id;
    private Identity identity;
    private Spend spending;
    private String country;

    @MongoObjectId
    String _id;

    public PurchaseManagement(){}

    public PurchaseManagement(JSONObject spend) throws Exception {
        //mettre la condition avec le seuil du prix des pays et en fonctions
        this.id = spend.getInt("id");
        this.country = spend.getString("country");
        this.identity = new Identity(spend.getJSONObject("identity"));
        this.status = Status.EN_ATTENTE;
        this.spending = new Spend(spend.getJSONObject("spend"));
    }



        public JSONObject toJson(){
            JSONObject result = new JSONObject();
            result.put("status", this.status.getStr());
            result.put("id", this.id);
            result.put("identity", this.identity.toJson());
            result.put("spend", this.spending.toJson());
            result.put("country",this.country);

            /*if (this.spending != null) {
                String z = "{";
                for (int i = 0; i < spending.size(); i++) {
                    z = z + spending.get(i).toJson();
                }
                z = z + "}";
                result.put("spends", z);
            }*/
            return result;
        }



        public void getTotalSpendings(){
            /*double price = 0;
            for (Spend spending : spending){
                price += spending.getPrix();
            }*/
            //return spending.getPrix();
        }



        @Override
        public String toString() {
            return "Purchase Management{" +
                    ", id=" + id +
                    "status=" + status +
                    ", identity=" + identity +
                    ", spendings ='" + spending + '\'' +
                    ", country ='" + country + '\'' +
                    '}';
        }



}