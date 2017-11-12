package travelcosts.handler;


import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.json.JSONObject;
import travelcosts.BillManagement.BillManagement;
import travelcosts.BillManagement.Spend;
import travelcosts.BillManagement.Status;
import travelcosts.network.SubmitSpend;
import travelcosts.seuil.Seuil;

import java.io.*;


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
            spends.update("{id:#}", idToValidate).with("{$set: {'status': 'VALIDE'}}");

            BillManagement Spends = spends.findOne("{id:#}", idToValidate).as(BillManagement.class);
            String mySpends = Spends.toString();
            archiver("depences_"+idToValidate,mySpends);

            return new JSONObject()
                    .put("approved", true)
                    .put("id", idToValidate);
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

            BillManagement Spends = spends.findOne("{id:#}", idToReject).as(BillManagement.class);
            String mySpends = Spends.toString();
            archiver("depences_"+idToReject,mySpends);

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
                    newtotalSeuil +=  new Seuil().calculateSeuil(myNewSpends[i].getCountry(),myNewSpends[i].getDate(),myNewSpends[i].getPrice().getCurrency());
                }
            }
            Spends.setTotalSpends(newtotalSpend);
            Spends.setTotalSeuil(newtotalSeuil);
            Spends.setSpends(myNewSpends);
            if(Spends.getTotalSpends()<= Spends.getTotalSeuil()){
                Spends.setStatus(Status.VALIDE);
            }else{
                Spends.setStatus(Status.EN_ATTENTE);
            }
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

    private void archiver(String archiveName,String mySpens){
        File file = new File(archiveName);
        try {
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mySpens);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
