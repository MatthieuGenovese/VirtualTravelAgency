package submittravel.data.tickets;

public class Planeticket extends ticket {

        private String departure_date="";
        private String departure_place="";
        private boolean escale=false;
        private String destination_place="";
        private String seat="";
        //private String name="";
        //private float prix=0;



        public Planeticket(String namepassager,String departure_date,String departure_place,boolean escale,String destination_place,String seat,float prix){
            this.departure_date=departure_date;
            this.departure_place=departure_place;
            this.escale=escale;
            this.destination_place=destination_place;
            this.seat=seat;
            this.name=namepassager;
            this.prix=prix;
        }

        public Planeticket(){};

    public String toString() {
        return "name:" + name + "  departure date:" + departure_date+ "  departure place:"+departure_place+"  escale:"+escale+"  destination date:"+departure_place+"  seat:"+seat+"  prix:"+prix;
    }
}
