package flyreservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Storage {

    // this mocks a database.
    private static HashMap<String, FlyReservation> contents = new HashMap<String, FlyReservation>();

    public static void create(String destination, String date, boolean isDirect, ArrayList<String> stops, double price) {
        contents.put(destination + date + Double.toString(price), new FlyReservation(destination, date, isDirect, stops, price));
    }

    public static FlyReservation read(String destination, String date, double price) {
        return contents.get(destination + date + Double.toString(price));
    }

    public static void delete(String destination, String date, double price) {
        contents.remove(destination + date + Double.toString(price));
    }

    public static Collection<FlyReservation> findAll() {
        return contents.values();
    }


    static {
        Storage.create("demofly", "28122018", true, new ArrayList<String>(), 1548.58);
    }

}