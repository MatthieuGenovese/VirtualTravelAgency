package esb.flows.technical.utils;

import esb.flows.technical.data.*;
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
            ItemInterface newf = (ItemInterface) oldEx.getIn().getBody();
            if(Integer.valueOf(newf.getPrice()) == Integer.MAX_VALUE){
                try{
                    Flight f = (Flight) newf;
                    f.setPrice("0");
                    oldEx.getIn().setBody(f);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
                try{
                    Car c = (Car) newf;
                    c.setPrice("0");
                    oldEx.getIn().setBody(c);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
                try{
                    Hotel h = (Hotel) newf;
                    h.setPrice("0");
                    oldEx.getIn().setBody(h);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
            }
            return oldEx;
        }
        else if (newEx != null){
            ItemInterface oldf = (ItemInterface) oldEx.getIn().getBody();
            ItemInterface newf = (ItemInterface) newEx.getIn().getBody();
            if(Integer.valueOf(newf.getPrice()) < Integer.valueOf(oldf.getPrice())){
                oldEx.getIn().setBody(newf);
            }
            else if(Integer.valueOf(newf.getPrice()) == Integer.MAX_VALUE){
                try{
                    Flight f = (Flight) newf;
                    f.setPrice("0");
                    oldEx.getIn().setBody(f);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
                try{
                    Car c = (Car) newf;
                    c.setPrice("0");
                    oldEx.getIn().setBody(c);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
                try{
                    Hotel h = (Hotel) newf;
                    h.setPrice("0");
                    oldEx.getIn().setBody(h);
                    return oldEx;
                }
                catch(ClassCastException e){

                }
            }
        }
        return oldEx;
    }
}
