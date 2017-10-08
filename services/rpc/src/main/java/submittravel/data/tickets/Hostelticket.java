package submittravel.data.tickets;

public class Hostelticket extends ticket {
    private String address="";
    private String start_date="";
    private String end_date="";


    public Hostelticket(String name,String address,String start_date,String end_date,float prix){
        this.address=address;
        this.start_date=start_date;
        this.end_date=end_date;
        this.name = name;
        this.prix = prix;
    }

    public Hostelticket(){};

    @Override
    public String toString() {
        return "HostelTicket{\n name:"+name+"  address:" + address + "  start date:" + start_date+ " end_date:"+end_date+"  prix:"+prix+"}";
    }
}
