package travelcosts.handler;


import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.json.JSONObject;
import travelcosts.Purchase.BillManagement;
import travelcosts.Purchase.Spend;
import travelcosts.Purchase.Status;
import travelcosts.network.SubmitSpend;

public class Handler {

    public JSONObject submitSpends(JSONObject spendsAsJson)
    {
        try {
            BillManagement spends = new BillManagement(spendsAsJson);
            MongoCollection Spends = SubmitSpend.mongoConnector.getSpends();
            Spends.insert(spends);
            return new JSONObject()
                    .put("inserted", true)
                    .put("bills", spends.toJson());
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return new JSONObject()
                    .put("inserted", false)
                    .put("message", e.getMessage());
        }
    }



    public JSONObject approveSpends(int idToValidate)
    {
        try {
            MongoCollection spends = SubmitSpend.mongoConnector.getSpends();
            spends.update("{id:#}", idToValidate).with("{$set: {'status': 'APPROVED'}}");
            return new JSONObject()
                    .put("id", idToValidate)
                    .put("approved", true);
            }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject()
                    .put("approved", false)
                    .put("id", idToValidate)
                    .put("message", "Error occured: " + e.getMessage());
        }
    }



    public JSONObject rejectSpends(int idToReject)
    {
        try {
            MongoCollection spends = SubmitSpend.mongoConnector.getSpends();
            spends.update("{id:#}", idToReject).with("{$set: {'status': 'REJECTED'}}");
            return new JSONObject()
                    .put("rejected", true)
                    .put("id", idToReject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject()
                    .put("rejected", false)
                    .put("id", idToReject)
                    .put("message", "Error occured: " + e.getMessage());
        }
    }


    public JSONObject retrieveSpends(int idToRetrieve)
    {
        try {
            MongoCollection spends = SubmitSpend.mongoConnector.getSpends();
            BillManagement Spends =spends.findOne("{id:#}", idToRetrieve).as(BillManagement.class);
            return Spends.toJson();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject()
                    .put("retrieved", false)
                    .put("id", idToRetrieve)
                    .put("message", "Error occured: " + e.getMessage());
        }
    }


    public JSONObject addJustificationSpends(int idToAddJustification,String justification)
    {
        try {
            MongoCollection spends = SubmitSpend.mongoConnector.getSpends();
            String set = "{$set: {'justification': '"+justification+"'}}";
            spends.update("{id:#}", idToAddJustification).with(set);
            return new JSONObject()
                    .put("justification", true)
                    .put("id", idToAddJustification);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject()
                    .put("justification", false)
                    .put("id", idToAddJustification)
                    .put("message", "Error occured: " + e.getMessage());
        }
    }



    public JSONObject purge() {
        MongoCollection spends = SubmitSpend.mongoConnector.getSpends();
            spends.drop();
            return new JSONObject().put("purge", "done");
    }

    public JSONObject addSpend(int idToAddSpends,JSONObject spendAsJson)
    {
        try {
            MongoCollection mangospends = SubmitSpend.mongoConnector.getSpends();
            BillManagement Spends =mangospends.findOne("{id:#}", idToAddSpends).as(BillManagement.class);
            Spend[] myOldSpends = Spends.getSpends();
            Spend[] myNewSpends = new Spend[myOldSpends.length + 1];
            double newtotalSeuil = Spends.getTotalSeuil();
            double newtotalSpend = Spends.getTotalSpends();
            Status newStatus;
            for (int i = 0; i < myOldSpends.length + 1; i++) {
                if(i<myOldSpends.length){
                    myNewSpends[i] = myOldSpends[i];
                }else{
                    myNewSpends[i] = new Spend(spendAsJson);
                    newtotalSpend += myNewSpends[i].getPrice().getPrice();
                }
            }
            //this.totalSeuil += seuil.calculateSeuil(s.getCountry(),s.getDate(),s.getPrice().getCurrency());
            //Spend = spendAsJson.getDouble("prix");
            //double totalspends = Spends.getTotalSpends() + spendAsJson.getDouble("prix");
            Spends.setTotalSpends(newtotalSpend);
            Spends.setSpends(myNewSpends);
            mangospends.remove(new ObjectId(Spends._id));
            mangospends.insert(Spends);
            return Spends.toJson();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new JSONObject()
                    .put("addSpends", false)
                    .put("id", idToAddSpends)
                    .put("message", "Error occured: " + e.getMessage());
        }
    }



}
