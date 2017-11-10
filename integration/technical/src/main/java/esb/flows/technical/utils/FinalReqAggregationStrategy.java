package esb.flows.technical.utils;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class FinalReqAggregationStrategy  implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldEx, Exchange newEx) {
        if(oldEx == null){
            oldEx = newEx;
            TravelAgencyRequest tr = new TravelAgencyRequest();
            switch(newEx.getIn().getHeader("type").toString()){
                case "car" :
                    tr.setCarReq((Car) newEx.getIn().getBody());
                    if(tr.getCarReq().getDate().equalsIgnoreCase("err")){
                        oldEx.getIn().setHeader("car", "erreur");
                    }
                    else {
                        oldEx.getIn().setHeader("car", "valid");
                    }
                    break;
                case "hotel" :
                    tr.setHotelReq((Hotel) newEx.getIn().getBody());
                    if(tr.getHotelReq().getName().equalsIgnoreCase("err")){
                        oldEx.getIn().setHeader("hotel", "erreur");
                    }
                    else {
                        oldEx.getIn().setHeader("hotel", "valid");
                    }
                    break;
                case "flight" :
                    tr.setFlightReq((Flight) newEx.getIn().getBody());
                    if(tr.getFlightReq().getDate().equalsIgnoreCase("err")){
                        oldEx.getIn().setHeader("flight", "erreur");
                    }
                    else {
                        oldEx.getIn().setHeader("flight", "valid");
                    }
                    break;
            }
            oldEx.getIn().setBody(tr);
        }
        else{
            TravelAgencyRequest tr2 = (TravelAgencyRequest) oldEx.getIn().getBody();
            switch(newEx.getIn().getHeader("type").toString()){
                case "car" :
                        tr2.setCarReq((Car) newEx.getIn().getBody());
                        if(tr2.getCarReq().getDate().equalsIgnoreCase("err")){
                            oldEx.getIn().setHeader("car", "erreur");
                        }
                        else {
                            oldEx.getIn().setHeader("car", "valid");
                        }
                        break;
                case "hotel" :
                        tr2.setHotelReq((Hotel) newEx.getIn().getBody());
                        if(tr2.getHotelReq().getName().equalsIgnoreCase("err")){
                            oldEx.getIn().setHeader("hotel", "erreur");
                        }
                        else {
                            oldEx.getIn().setHeader("hotel", "valid");
                        }
                        break;
                case "flight" :
                        tr2.setFlightReq((Flight) newEx.getIn().getBody());
                        if(tr2.getFlightReq().getDate().equalsIgnoreCase("err")){
                            oldEx.getIn().setHeader("flight", "erreur");
                        }
                        else {
                            oldEx.getIn().setHeader("flight", "valid");
                        }
                        break;
            }
            oldEx.getIn().setBody(tr2);
        }
        TravelAgencyRequest state = (TravelAgencyRequest) oldEx.getIn().getBody();
        if(state.getFlightReq() != null && state.getHotelReq() != null && state.getCarReq() != null){
            String stateFlight = (String) oldEx.getIn().getHeader("flight");
            String stateCar = (String) oldEx.getIn().getHeader("car");
            String stateHotel = (String) oldEx.getIn().getHeader("hotel");
            if(stateCar.equalsIgnoreCase("erreur")
                    || stateFlight.equalsIgnoreCase("erreur")
                    || stateHotel.equalsIgnoreCase("erreur")){
                oldEx.getIn().setHeader("etat", "erreur");
            }
            else if (state.getCarReq().getDestination().equalsIgnoreCase("not found")){
                oldEx.getIn().setHeader("etat", "voiturenotfound");
            }
            else if (state.getHotelReq().getDestination().equalsIgnoreCase("not found")){
                oldEx.getIn().setHeader("etat", "hotelnotfound");
            }
            else if (state.getFlightReq().getDestination().equalsIgnoreCase("not found")){
                oldEx.getIn().setHeader("etat", "flightnotfound");
            }
            else{
                oldEx.getIn().setHeader("etat", "valid");
            }
        }
        return oldEx;
    }
}
