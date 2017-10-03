package carshostelsreservation;

import org.json.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;


@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CarHotelReservationService {

    @Path("/cars")
    @GET
    public Response getCarsWithParam(@QueryParam("date") String date, @QueryParam("dest") String dest) {
        if(date == null){
            date = "";
        }
        if(dest == null){
            dest = "";
        }
        if(Storage.readCar(dest,date) == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        JSONArray result = new JSONArray();
        for (CarReservation c : Storage.readCar(dest, date)){
            result.put(c.toJson());
        }
        return Response.ok().entity(result.toString(2)).build();

    }

    @Path("/hotels")
    @GET
    public Response getHotelsWithParam(@QueryParam("date") String date, @QueryParam("dest") String dest) {
        if(date == null){
            date = "";
        }
        if(dest == null){
            dest = "";
        }
        if(Storage.readHotel(dest,date) == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        JSONArray result = new JSONArray();
        for (HotelReservation h : Storage.readHotel(dest, date)){
            result.put(h.toJson());
        }
        return Response.ok().entity(result.toString(2)).build();

    }

}
