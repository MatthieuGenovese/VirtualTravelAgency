package flyreservation;


import org.apache.commons.beanutils.BeanComparator;


import java.util.*;

public class Storage {

    // this mocks a database.
    private static HashMap<String, FlyReservation> contents = new HashMap<String, FlyReservation>();

    public static void create(String destination, String date, boolean isDirect, ArrayList<String> stops, double price) {
        contents.put(destination + date + Double.toString(price), new FlyReservation(destination, date, isDirect, stops, price));
    }

    public static ArrayList<FlyReservation> read(String destination, String date) {
        ArrayList<FlyReservation> results = new ArrayList<>();
        for(FlyReservation f : contents.values()){
            if(f.getDate().equalsIgnoreCase(date) && f.getDestination().equalsIgnoreCase(destination)){
                results.add(f);
            }
        }
        BeanComparator fieldComparator = new BeanComparator(
                "price");
        Collections.sort(results, fieldComparator);
        return results;
    }

    public static void delete(String destination, String date, double price) {
        contents.remove(destination + date + Double.toString(price));
    }

    public static Collection<FlyReservation> findAll() {
        return contents.values();
    }


    static {
        ArrayList<String> l1 = new ArrayList<>();
        ArrayList<String> l2 = new ArrayList<>();
        l1.add("Nancy");
        l1.add("Nice");
        l1.add("Berlin");
        l2.add("Nancy");
        l2.add("Nice");
        Storage.create("paris", "28122018", true, new ArrayList<String>(), 1654.58);
        Storage.create("paris", "28122018", false, l1, 1648.58);
        Storage.create("paris", "28122018", false, l2, 1600.58);
        Storage.create("nice", "28122018", true, new ArrayList<String>(), 1500.58);
        Storage.create("nice", "28122018", false, l1, 1560.58);
        Storage.create("nice", "28122018", false, l2, 1530.58);
    }

}