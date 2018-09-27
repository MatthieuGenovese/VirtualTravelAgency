package orderfood;

import org.jongo.marshall.jackson.oid.MongoObjectId;
import org.json.JSONObject;
import java.util.Random;

public class OrderFood {
    private String id;

    private String nameOfFood;

    private String nameOfClient;

    private String addressDestination;

    private String deliveryTime;

    private String status;


    @MongoObjectId
    String _id;

    public OrderFood(JSONObject data) {
        this.id = data.getString("id");
        this.nameOfFood = data.getString("nameOfFood");
        this.nameOfClient = data.getString("nameOfClient");
        this.addressDestination = data.getString("addressDestination");
        this.deliveryTime = calculateDeliveryTime()+" min";
        this.status = "Processing";
    }

    public OrderFood() {} // ne pas enlever, c'est pour instancier la class avec findOne(...OrderFood.class)


    public double calculateDeliveryTime(){
        Random rand = new Random();
        int min = 20;
        int max = 120;
        double delivery = min + rand.nextInt(max - min + 1);
        return delivery;
    }


    JSONObject toJson() {
        return new JSONObject()
                .put("id", id)
                .put("nameOfFood", nameOfFood)
                .put("nameOfClient", nameOfClient)
                .put("addressDestination", addressDestination)
                .put("deliveryTime", deliveryTime)
                .put("status", status);
    }

}
