package esb.flows.technical;

import org.apache.camel.builder.RouteBuilder;

import static esb.flows.technical.utils.Endpoints.DEATH_POOL;

public class DeathPool extends RouteBuilder{
    static int REDELIVERIES = 2;

    @Override
    public void configure() throws Exception {

        errorHandler(
                deadLetterChannel(DEATH_POOL)
                        .useOriginalMessage()
                        .maximumRedeliveries(REDELIVERIES)
                        .redeliveryDelay(500)
        );
        /*from(BAD_CITIZEN)
                .log("Bad information for citizen ")
                .log("${body}")
        ;*/
    }
}
