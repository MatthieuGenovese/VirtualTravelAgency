/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservation;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.json.JSONArray;
import org.json.JSONObject;


class Handler {

    static JSONObject register(JSONObject input) {
        MongoCollection vols = getVols();
        FlightReservation data = new FlightReservation(input.getJSONObject("flightreservation"));
        vols.insert(data);
        return new JSONObject().put("inserted", true).put("flightreservation",data.toJson());
    }
    
    static JSONObject delete(JSONObject input) {
        MongoCollection vols = getVols();
        String ssn = input.getString("id");
        FlightReservation theOne = vols.findOne("{id:#}",ssn).as(FlightReservation.class);
        if (null == theOne) {
            return new JSONObject().put("deleted", false);
        }
        vols.remove(new ObjectId(theOne._id));
        return new JSONObject().put("deleted", true);
    }

    static JSONObject list(JSONObject input) {
        MongoCollection vols = getVols();
        JSONObject filter = input.getJSONObject("filter");
        String liste = "";
        for(int i=0;i<filter.length();i++){
            liste += filter.toString(i);
        }
        MongoCursor<FlightReservation> cursor =
                vols.find(liste).as(FlightReservation.class);
        List array = new ArrayList();
        JSONArray contents = new JSONArray(); int size = 0;
        while(cursor.hasNext()) {
            array.add(cursor.next().toJson()); size++;
//            contents.put(cursor.next().toJson()); size++;
        }
        Collections.sort(array, new Comparator<JSONObject>() {
            private static final String KEY_NAME = "price";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                valA = (String) a.get(KEY_NAME);
                valB = (String) b.get(KEY_NAME);

                return valA.compareTo(valB);
            }
        });
        for (int i = 0; i < array.size(); i++) {
            contents.put(array.get(i));
        }
        return new JSONObject().put("size", size).put("vols", contents);
    }

    static JSONObject dump(JSONObject input) {
        return list(new JSONObject().put("filter",new JSONObject()));
    }

    static JSONObject purge(JSONObject input) {
        MongoCollection vols = getVols();
        if(input.getString("use_with").equals("caution")) {
            vols.drop();
            return new JSONObject().put("purge", "done");
        }
        throw new RuntimeException("Safe word does not match what is expected!");
    }

    static JSONObject retrieve(JSONObject input) {
        MongoCollection vols = getVols();
        String ssn = input.getString("id");
        FlightReservation theOne = vols.findOne("{id:#}",ssn).as(FlightReservation.class);
        if (theOne == null) {
            throw new RuntimeException("No match found for " + ssn);
        }
        return theOne.toJson();
    }
    
    private static MongoCollection getVols() {
        MongoClient client = new MongoClient(Network.HOST, Network.PORT);
        return new Jongo(client.getDB(Network.DATABASE)).getCollection(Network.COLLECTION);
    }
}
