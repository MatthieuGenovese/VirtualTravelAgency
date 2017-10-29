package travelcosts.Purchase;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONObject;
import travelcosts.date.DateInterval;

import java.util.Iterator;
import java.util.List;

public class PurchaseManagement {
    private Status status;
    private int id;
    private Identity identity;
    private DateInterval dateInterval;
    private List<Spend> spendings;

    @MongoObjectId
    String _id;

    public PurchaseManagement(){}

    public PurchaseManagement(JSONObject spend) throws Exception {
        this.status = Status.EN_ATTENTE;
        this.id = spend.getInt("id");
        this.identity = new Identity(spend.getJSONObject("identity"));
        try {
            JSONObject spends = spend.getJSONObject("spends");
            for (Iterator iterator = spends.keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                System.out.println(spend.get(key));
            }
        } catch (NullPointerException e) {
            this.spendings = null;
        }
    }



        public JSONObject toJson(){
            JSONObject result = new JSONObject();
            result.put("status", this.status.getStr());
            result.put("id", this.id);
            result.put("identity", this.identity.toJson());
            if (this.spendings != null) {
                String z = "{";
                for (int i = 0; i < spendings.size(); i++) {
                    z = z + spendings.get(i).toJson();
                }
                z = z + "}";
                result.put("spends", z);
            }
            return result;
        }



        public double getTotalSpendings(){
            double price = 0;
            for (Spend spending : spendings){
                price += spending.getPrix();
            }
            return price;
        }



        @Override
        public String toString() {
            return "Purchase Management{" +
                    ", id=" + id +
                    "status=" + status +
                    ", identity=" + identity +
                    ", spendings ='" + spendings + '\'' +
                    '}';
        }



}