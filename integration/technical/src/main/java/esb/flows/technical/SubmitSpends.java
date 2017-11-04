package esb.flows.technical;

import esb.flows.technical.data.Identity;
import esb.flows.technical.data.Spend;
import esb.flows.technical.data.SpendRequest;
import esb.flows.technical.utils.CsvFormat;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

public class SubmitSpends extends RouteBuilder{

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2);

    @Override
    public void configure() throws Exception {
        from(FILE_INPUT_SPEND)
                .routeId("refund-request")
                .routeDescription("demande de remboursement")
                .setHeader(Exchange.HTTP_METHOD, constant("POST")) // on choisis le type de requete (ici du POST en json)
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                    .split(body()) // on effectue un travaille en parralele sur la map >> on transforme tout ca en objet de type Flight
                    .parallelProcessing().executorService(WORKERS)
                    .process(csv2spendreq)
                .marshal().json(JsonLibrary.Jackson)
                .unmarshal().string()
                .process(spendreq2jsonservice)
                .log(body().toString())
                .inOut(SPENDSERVICE_ENDPOINT)
                .log(body().toString())
        ;


    }

    private static Processor spendreq2jsonservice = (Exchange exchange) -> {
        String recv = (String) exchange.getIn().getBody();
        String header = "{\"type\":\"submit\",\"spends\":";
        String endofReq = "}";
        exchange.getIn().setBody(header+recv+endofReq);

    };

    private static Processor csv2spendreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        Spend s =  new Spend();
        Identity i = new Identity();
        SpendRequest sr = new SpendRequest();
        s.setId((String) data.get("id"));
        s.setDate((String) data.get("date"));
        s.setPrix((String) data.get("prix"));
        s.setReason((String) data.get("reason"));
        i.setEmail((String) data.get("email"));
        i.setFirstName((String) data.get("firstName"));
        i.setLastName((String) data.get("lastName"));
        sr.setId((String) data.get("idGlobale"));
        sr.setIdentity(i);
        sr.setSpend(s);
        exchange.getIn().setBody(sr);
        //exchange.getIn().setHeader("requete-id", (String) data.get("id"));
    };


}
