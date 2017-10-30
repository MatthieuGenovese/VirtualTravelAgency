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

    @MongoObjectId
    String _id;

    public PurchaseManagement(){}

    public PurchaseManagement(JSONObject spend) throws Exception {
        //mettre la condition avec le seuil du prix des pays et en fonctions
        this.status = Status.EN_ATTENTE;
        this.id = spend.getInt("id");
        this.identity = new Identity(spend.getJSONObject("identity"));
        try {
            //JSONObject spends = spend.getJSONObject("spend");
            /*for (Iterator iterator = spends.keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                System.out.println(spend.get(key));*/
            this.spending = new Spend(spend.getJSONObject("spend"));
        } catch (NullPointerException e) {
            this.spending = null;
        }
    }



        public JSONObject toJson(){
            JSONObject result = new JSONObject();
            result.put("status", this.status.getStr());
            result.put("id", this.id);
            result.put("identity", this.identity.toJson());
            result.put("spend", this.spending.toJson());

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



        public double getTotalSpendings(){
            /*double price = 0;
            for (Spend spending : spending){
                price += spending.getPrix();
            }*/
            return spending.getPrix();
        }



        @Override
        public String toString() {
            return "Purchase Management{" +
                    ", id=" + id +
                    "status=" + status +
                    ", identity=" + identity +
                    ", spendings ='" + spending + '\'' +
                    '}';
        }



}