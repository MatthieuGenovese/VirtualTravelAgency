package travelcosts.Purchase;

import org.json.JSONObject;

public class Identity {


    private String firstName;
    private String lastName;
    private String email;


    public Identity(JSONObject data){
        System.out.println("Identity: " + data);
        this.firstName = data.getString("firstName");
        this.lastName = data.getString("lastName");
        this.email = data.getString("email");
    }


    JSONObject toJson(){
        return new JSONObject()
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("email", email);
    }


}
