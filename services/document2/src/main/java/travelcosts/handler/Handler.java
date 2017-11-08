package travelcosts.handler;


import org.jongo.MongoCollection;
import org.json.JSONObject;
import travelcosts.Purchase.PurchaseManagement;
import travelcosts.network.SubmitSpend;

import java.io.FileWriter;

public class Handler {

    public JSONObject submitSpends(JSONObject spendsAsJson)
    {
        try {
            PurchaseManagement spends = new PurchaseManagement(spendsAsJson);
            MongoCollection Spends = SubmitSpend.mongoConnector.getSpends();
            Spends.insert(spends);
            return new JSONObject()
                    .put("inserted", true)
                    .put("spends", spends.toJson());
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
            PurchaseManagement Spends =spends.findOne("{id:#}", idToRetrieve).as(PurchaseManagement.class);
            return Spends.toJson();//.put("retrieved", true);
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




}
