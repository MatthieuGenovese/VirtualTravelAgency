package submittravel.data;

import submittravel.data.tickets.Carticket;
import submittravel.data.tickets.Hostelticket;
import submittravel.data.tickets.Planeticket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class TravelRequest {
    private String id_employe;
    private Carticket carticket;
    private Hostelticket hostelticket;
    private Planeticket planeticket;


    @XmlElement(name = "id", required = true)
    public String getIdemploye() { return id_employe; }
    public void setIdemploye(String idemploye) { this.id_employe = idemploye; }



    @XmlElement(required=true)
    public Carticket getCarticket() { return carticket; }
    public void setCarticket(Carticket carticket) { this.carticket = carticket; }

    @XmlElement(required=true)
    public Hostelticket getHostelticket() { return hostelticket; }
    public void setHostelticket(Hostelticket hostelticket) { this.hostelticket = hostelticket; }

    @XmlElement(required=true)
    public Planeticket getPlaneticket() { return planeticket; }
    public void setPlaneticket(Planeticket planeticket) { this.planeticket = planeticket; }


    public String toString() {
        return "TravelRequest:\n  Id Employe: " + id_employe + "\n  Car ticket: " + carticket.toString() +"\n  Hostel ticket:  "+ hostelticket.toString()+"\n  Plane ticket:  "+planeticket.toString();
    }



}
