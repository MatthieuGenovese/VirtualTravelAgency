package esb.flows.technical.utils;

import esb.flows.technical.data.*;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class FinalReqAggregationStrategy  implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldEx, Exchange newEx) {
        if(oldEx == null){
            TravelAgencyRequest tr = new TravelAgencyRequest();
            oldEx = newEx;
            oldEx.getIn().setBody(tr);
        }
        else{
            TravelAgencyRequest tr2 = (TravelAgencyRequest) oldEx.getIn().getBody();
            switch(newEx.getIn().getHeader("type").toString()){
                case "car" :
                        tr2.setCarReq((Car) newEx.getIn().getBody());
                        break;
                case "hotel" :
                        tr2.setHotelReq((Hotel) newEx.getIn().getBody());
                        break;
                case "flight" :
                        tr2.setFlightReq((Flight) newEx.getIn().getBody());
                        break;
            }
            oldEx.getIn().setBody(tr2);
        }
        return oldEx;
    }
}
