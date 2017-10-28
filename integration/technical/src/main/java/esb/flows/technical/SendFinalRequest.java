package esb.flows.technical;

import esb.flows.technical.data.TravelAgencyRequest;
import esb.flows.technical.utils.FinalReqAggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
            //.marshal().json(JsonLibrary.Jackson)
            .to(REQUETE_QUEUE)

        ;
    }

    private static boolean finish(Exchange exc){
        TravelAgencyRequest tr = (TravelAgencyRequest) exc.getIn().getBody();
        if(tr.getCarReq() != null && tr.getFlightReq() != null && tr.getHotelReq() != null){
            System.out.println("fini !");
            return true;
        }
        return false;
    }
    private static Processor test = (Exchange exchange) -> {
        TravelAgencyRequest tr = (TravelAgencyRequest) exchange.getIn().getBody();
        System.out.println("PRINTSAMERE : " + tr.getCarReq() + " " + tr.getFlightReq() + " " + tr.getCarReq());
        exchange.getIn().setBody(exchange.getIn().getBody());

    };

    private static Processor test2 = (Exchange exchange) -> {
        System.out.println("PRINT LES REQUETES : " + exchange.getIn().getBody());
        System.out.println("PRINT LES HEADERS : " + exchange.getIn().getHeaders());
        exchange.getIn().setBody(exchange.getIn().getBody());

    };
}
