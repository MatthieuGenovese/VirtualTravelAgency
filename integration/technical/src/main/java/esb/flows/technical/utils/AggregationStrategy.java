package esb.flows.technical.utils;

import org.apache.camel.Exchange;

import java.util.Map;

/**
 * Created by Jerem on 22/10/2017.
 */
public class AggregationStrategy implements org.apache.camel.processor.aggregate.AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange exchangeA, Exchange exchangeB) {
        Map<String, Object> dataA = (Map <String, Object>) exchangeA.getIn().getBody();
        Map<String, Object> dataB = (Map <String, Object>) exchangeB.getIn().getBody();
        int prixA = (int) dataA.get("price");
        int prixB = (int) dataB.get("price");

        if(prixA < prixB){
            return exchangeA;
        }
        return exchangeB;

    }
}
