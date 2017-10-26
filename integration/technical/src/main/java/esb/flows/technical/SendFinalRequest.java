package esb.flows.technical;

import esb.flows.technical.data.TravelAgencyRequest;
import esb.flows.technical.utils.FinalReqAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import static esb.flows.technical.utils.Endpoints.*;

public class SendFinalRequest extends RouteBuilder {

    @Override
    public void configure() throws Exception{
        from(AGGREG_TRAVELREQUEST)
            .routeId("aggre-final-request")
            .routeDescription("Concatenation des 3 demandes vol, car et hotel")
            .aggregate(constant(true), new FinalReqAggregationStrategy())
                .completionPredicate(SendFinalRequest::finish)
            .marshal().json(JsonLibrary.Jackson)
            .to(CAMEL_FINAL)

        ;
    }

    private static boolean finish(Exchange exc){
        TravelAgencyRequest tr = (TravelAgencyRequest) exc.getIn().getBody();
        if(tr.getCarReq() != null && tr.getFlightReq() != null && tr.getHotelReq() != null){
            return true;
        }
        return false;
    }
}
