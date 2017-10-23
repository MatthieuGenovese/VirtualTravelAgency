package esb.flows.technical.utils;

import esb.flows.technical.data.Flight;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.ItemInterface;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.Map;

/**
 * Created by Jerem on 22/10/2017.
 */
public class FlightCarHotelAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldEx, Exchange newEx) {
        if(oldEx == null){
            oldEx = newEx;
            return newEx;
        }
        else if (newEx != null){
            ItemInterface oldf = (ItemInterface) oldEx.getIn().getBody();
            ItemInterface newf = (ItemInterface) newEx.getIn().getBody();
            if(Integer.valueOf(newf.getPrice()) < Integer.valueOf(oldf.getPrice())){
                oldEx.getIn().setBody(newf);
            }
            else {
                return oldEx;
            }
        }
        return oldEx;
    }
}
