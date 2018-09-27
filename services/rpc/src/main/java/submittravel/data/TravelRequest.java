package submittravel.data;

import submittravel.data.tickets.Carticket;
import submittravel.data.tickets.Hostelticket;
import submittravel.data.tickets.Planeticket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TravelRequest {
    private String start_date_car="";
    private String end_date_car="";
    private String model="";
    private String address_hostel="";
    private String start_date_hostel="";
    private String end_date_hostel="";
    private String departure_date_plane="";
    private String departure_place_plane="";
    private String destination_place_plane="";
    private String seat_plane="";
    private String price_plane="0";
    private String price_hostel="0";
    private String price_car="0";


    private String id_employe;


    private Carticket carticket;
    private Hostelticket hostelticket;
    private Planeticket planeticket;


    @XmlElement(name = "id", required = true)
    public String getIdemploye() { return id_employe; }
    public void setIdemploye(String idemploye) { this.id_employe = idemploye; }

    /*@XmlElement(name = "Carticket",required=true)
    public String getCarticket() { return carticket.toString(); }
    public void setCarticket(Carticket carticket) { this.carticket = carticket; }

    @XmlElement(name = "Hostelticket",required=true)
    public String getHostelticket() { return hostelticket.toString(); }
    public void setHostelticket(Hostelticket hostelticket) { this.hostelticket =hostelticket; }

    @XmlElement(name = "Planeticket",required=true)
    public String getPlaneticket() { return planeticket.toString(); }
    public void setPlaneticket(String namepassager,String departure_date,String departure_place,boolean escale,String destination_place,String seat,float prix) { this.planeticket = new Planeticket(namepassager,departure_date,departure_place,escale,destination_place,seat,prix); }
    */

    public String toString() {
        return "TravelRequest:\n  Id Employe: " + id_employe + "\n  Car ticket: " + carticket.toString() +"\n  Hostel ticket:  "+ hostelticket.toString()+"\n  Plane ticket:  "+planeticket.toString();
    }


















    @XmlElement(required = false)
    public String getStart_date_car() { return start_date_car; }
    public void setStart_date_car(String start_date) { this.start_date_car = start_date; }

    @XmlElement(required = false)
    public String getEnd_date_car() { return end_date_car; }
    public void setEnd_date_car(String end_date) { this.end_date_car = end_date; }

    @XmlElement(required = false)
    public String getModel_car() { return model; }
    public void setModel_car(String model) { this.model = model; }


    @XmlElement(required = false)
    public String getAddress_hostel() { return address_hostel; }
    public void setAddress_hostel(String address_hostel) { this.address_hostel = address_hostel; }

    @XmlElement(required = false)
    public String getDestination_place_plane() { return destination_place_plane; }
    public void setDestination_place_plane(String destination_place_plane) { this.destination_place_plane = destination_place_plane; }

    @XmlElement(required = false)
    public String getSeat_plane() { return seat_plane; }
    public void setSeat_plane(String seat_plane) { this.seat_plane = seat_plane; }

    @XmlElement(required = false)
    public String getDeparture_place_plane() { return departure_place_plane; }
    public void setDeparture_place_plane(String departure_place_plane) { this.departure_place_plane = departure_place_plane; }

    @XmlElement(required = false)
    public String getDeparture_date_plane() { return departure_date_plane; }
    public void setDeparture_date_plane(String departure_date_plane) { this.departure_date_plane = departure_date_plane; }

    @XmlElement(required = false)
    public String getEnd_date_hostel() { return end_date_hostel; }
    public void setEnd_date_hostel(String end_date_hostel) { this.end_date_hostel = end_date_hostel; }

    @XmlElement(required = false)
    public String getStart_date_hostel() { return start_date_hostel; }
    public void setStart_date_hostel(String start_date_hostel) { this.start_date_hostel = start_date_hostel; }

    @XmlElement(required = false)
    public String getPrice_plane() { return price_plane; }
    public void setPrice_plane(String price_plane) { this.price_plane = price_plane; }

    @XmlElement(required = false)
    public String getPrice_hostel() { return price_hostel; }
    public void setPrice_hostel(String price_hostel) { this.price_hostel = price_hostel; }

    @XmlElement(required = false)
    public String getPrice_car() { return price_car; }
    public void setPrice_car(String price_car) { this.price_car = price_car; }
}
