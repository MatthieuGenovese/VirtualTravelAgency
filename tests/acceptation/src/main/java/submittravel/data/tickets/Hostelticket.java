package submittravel.data.tickets;

public class Hostelticket extends ticket {
    private String address="";
    private String start_date="";
    private String end_date="";
    //private String name="";
    //private float prix=0;

    public Hostelticket(String name,String address,String start_date,String end_date,float prix){
        this.address=address;
        this.start_date=start_date;
        this.end_date=end_date;
        this.name = name;
        this.prix = prix;
    }

    public Hostelticket(){};

    public String toString() {
        return "name:"+name+"  address:" + address + "  start date:" + start_date+ " end_date:"+end_date+"  prix:"+prix;
    }
}
