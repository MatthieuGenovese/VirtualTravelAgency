package esb.flows.technical.utils;

import esb.flows.technical.data.Flight;
import esb.flows.technical.data.FlightRequest;
import esb.flows.technical.data.ItemInterface;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

import java.util.Map;

/**
 * Created by Jerem on 22/10/2017.
 */
public class AggregationStrategy implements org.apache.camel.processor.aggregate.AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldEx, Exchange newEx) {
        System.out.println("fsdsd " + newEx.getIn().getBody());
        if(oldEx == null){
            oldEx = newEx;
            return newEx;
        }
        else if (newEx != null){
            Flight oldf = (Flight) oldEx.getIn().getBody();
            Flight newf = (Flight) newEx.getIn().getBody();
            System.out.println("fail1 : "+oldf);
            System.out.println("fail 2 : "+newf);
            if(Integer.valueOf(newf.getPrice()) < Integer.valueOf(oldf.getPrice())){
                oldEx.getIn().setBody(newf);
            }
            else {
                return oldEx;
            }
        }
        return oldEx;
    }

    private void add(Flight old, Message m){
        old = m.getBody(Flight.class);
    }
}
