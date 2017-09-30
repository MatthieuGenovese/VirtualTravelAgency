/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carshotels;
import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.json.JSONArray;
import org.json.JSONObject;


class Handler {

    static JSONObject register(JSONObject input) {
        MongoCollection hotels = getHotels();
        Hotel data = new Hotel(input.getJSONObject("hotel"));
        hotels.insert(data);
        return new JSONObject().put("inserted", true).put("hotel",data.toJson());
    }
    
    static JSONObject delete(JSONObject input) {
        MongoCollection hotels = getHotels();
        String ssn = input.getString("name");
        Hotel theOne = hotels.findOne("{name:#}",ssn).as(Hotel.class);
        if (null == theOne) {
            return new JSONObject().put("deleted", false);
        }
        hotels.remove(new ObjectId(theOne._id));
        return new JSONObject().put("deleted", true);
    }

    static JSONObject list(JSONObject input) {
        MongoCollection hotels = getHotels();
        String filter = input.getString("filter");
        MongoCursor<Hotel> cursor =
                hotels.find("{name: {$regex: #}}", filter).as(Hotel.class);
        JSONArray contents = new JSONArray(); int size = 0;
        while(cursor.hasNext()) {
            contents.put(cursor.next().toJson()); size++;
        }
        return new JSONObject().put("size", size).put("hotels", contents);
    }

    static JSONObject dump(JSONObject input) {
        return list(new JSONObject().put("filter",".*"));
    }

    static JSONObject purge(JSONObject input) {
        MongoCollection hotels = getHotels();
        if(input.getString("use_with").equals("caution")) {
            hotels.drop();
            return new JSONObject().put("purge", "done");
        }
        throw new RuntimeException("Safe word does not match what is expected!");
    }

    static JSONObject retrieve(JSONObject input) {
        MongoCollection hotels = getHotels();
        String ssn = input.getString("name");
        Hotel theOne = hotels.findOne("{name:#}",ssn).as(Hotel.class);
        if (theOne == null) {
            throw new RuntimeException("No match found for " + ssn);
        }
        return theOne.toJson();
    }
    
    private static MongoCollection getHotels() {
        MongoClient client = new MongoClient(Network.HOST, Network.PORT);
        return new Jongo(client.getDB(Network.DATABASE)).getCollection(Network.COLLECTION);
    }
}
