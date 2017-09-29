package flyreservation;

import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Path("/flyreservation")
@Produces(MediaType.APPLICATION_JSON)
public class FlyReservationService {


    @GET
    public Response getAllReservations(){
        Collection<FlyReservation> resas = Storage.findAll();
        JSONArray result = new JSONArray();
        for(FlyReservation f : resas){
            result.put(f.getID());
        }
        return Response.ok().entity(result.toString(2)).build();
    }
}
