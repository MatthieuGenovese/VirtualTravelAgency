package orderfood;
import com.mongodb.MongoClient;
import java.util.ArrayList;
import java.util.List;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.json.JSONObject;


class Handler {

    static JSONObject order(JSONObject input) {
        MongoCollection orders = getOrders();
        OrderFood data = new OrderFood(input.getJSONObject("orderFood"));
        orders.insert(data);
        return new JSONObject().put("inserted", true).put("orderFood",data.toJson());
    }

    static JSONObject validate(JSONObject input)
    {
        MongoCollection orders = getOrders();
        String id = input.getString("id");
        Boolean validate = input.getBoolean("validate");
        if(validate) {
            orders.update("{id:#}", id).with("{$set: {'status': 'VALIDATED'}}");
        }else {
            orders.update("{id:#}", id).with("{$set: {'status': 'NOT VALIDATED'}}");
        }
        OrderFood myOrder = orders.findOne("{id:#}",id).as(OrderFood.class);
        return new JSONObject().put("approved", validate).put("orderFood", myOrder.toJson());
    }

    static JSONObject consult(JSONObject input) {
        MongoCollection orders = getOrders();
        MongoCursor<OrderFood> cursor = orders.find().as(OrderFood.class);
        List array = new ArrayList();
        while(cursor.hasNext()) {
            array.add(cursor.next().toJson());
        }
        return new JSONObject().put("orders", array);
    }

    static JSONObject purge(JSONObject input) {
        MongoCollection orders = getOrders();
        if(input.getString("use_with").equals("caution")) {
            orders.drop();
            return new JSONObject().put("purge", "done");
        }
        throw new RuntimeException("Safe word does not match what is expected!");
    }
    
    private static MongoCollection getOrders() {
        MongoClient client = new MongoClient(Network.HOST, Network.PORT);
        return new Jongo(client.getDB(Network.DATABASE)).getCollection(Network.COLLECTION);
    }
}
