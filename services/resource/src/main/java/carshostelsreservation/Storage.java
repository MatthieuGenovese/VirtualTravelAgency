package carshostelsreservation;


import org.apache.commons.beanutils.BeanComparator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class Storage {

    // this mocks a database.
    private static HashMap<String, HotelReservation> contentsHotels = new HashMap<String, HotelReservation>();
    private static HashMap<String, CarReservation> contentsCars = new HashMap<String, CarReservation>();

    public static void createHotel(String name, int price, String date, String destination) {
        contentsHotels.put(destination + name, new HotelReservation(name, price, date, destination));
    }

    public static void createCar(String name, int price, String date, String destination) {
        contentsCars.put(destination + name, new CarReservation(name, price, date, destination));
    }

    public static ArrayList<CarReservation> readCar(String destination, String date) {
        ArrayList<CarReservation> results = new ArrayList<>();
        for(CarReservation c : contentsCars.values()){
            if((c.getDate().equalsIgnoreCase(date) || date.length() ==0) && (destination.length() == 0 || c.getDestination().equalsIgnoreCase(destination))){
                results.add(c);
            }
        }
        BeanComparator fieldComparator = new BeanComparator(
                "price");
        Collections.sort(results, fieldComparator);
        return results;
    }

    public static ArrayList<HotelReservation> readHotel(String destination, String date) {
        ArrayList<HotelReservation> results = new ArrayList<>();
        for(HotelReservation h : contentsHotels.values()){
            if((h.getDate().equalsIgnoreCase(date) || date.length() ==0) && (destination.length() == 0 || h.getDestination().equalsIgnoreCase(destination))){
                results.add(h);
            }
        }
        BeanComparator fieldComparator = new BeanComparator(
                "price");
        Collections.sort(results, fieldComparator);
        return results;
    }

    public static void deleteHotel(String destination, String name) {
        contentsHotels.remove(destination + name);
    }

    public static void deleteCar(String destination, String name) {
        contentsCars.remove(destination + name);
    }

    public static Collection<HotelReservation> findAllHotels() {
        return contentsHotels.values();
    }

    public static Collection<CarReservation> findAllCars() {
        return contentsCars.values();
    }


    static {
        Storage.createHotel("Hotel1", 180, "28/12/2017", "Paris");
        Storage.createHotel("Hotel2", 220, "28/11/2017", "Lyon");
        Storage.createHotel("Hotel3", 250, "28/11/2017", "Paris");
        Storage.createHotel("Hotel4", 30, "28/11/2017", "Ipaba");
        Storage.createCar("Car1", 60, "28/11/2017", "Lyon");
        Storage.createCar("Car2", 80, "28/12/2017", "Paris");
        Storage.createCar("Car3", 70, "28/11/2017", "Paris");
    }

}