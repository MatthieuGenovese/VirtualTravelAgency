package travelcosts.handler;


import org.jongo.MongoCollection;
import org.json.JSONObject;
import travelcosts.Purchase.PurchaseManagement;
import travelcosts.network.SubmitSpend;

public class Handler {

    public JSONObject submitSpends(JSONObject spendsAsJson)
    {
        try {

            MongoCollection Spends = SubmitSpend.mongoConnector.getBookings();
            PurchaseManagement spends = new PurchaseManagement(spendsAsJson);
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
            MongoCollection spends = SubmitSpend.mongoConnector.getBookings();
            spends.update("{id:#}", idToValidate).upsert().multi().with("{$set: {'status': 'APPROVED'}}");
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
            MongoCollection spends = SubmitSpend.mongoConnector.getBookings();
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
            MongoCollection spends = SubmitSpend.mongoConnector.getBookings();
            PurchaseManagement Spends =spends.findOne("{id:#}", idToRetrieve).as(PurchaseManagement.class);
            return Spends.toJson().put("retrieved", true);
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







}
