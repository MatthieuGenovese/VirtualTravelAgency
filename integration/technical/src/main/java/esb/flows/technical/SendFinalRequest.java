package esb.flows.technical;

import esb.flows.technical.data.ManagerAnswer;
import esb.flows.technical.data.TravelAgencyRequest;
import esb.flows.technical.utils.CsvFormat;
import esb.flows.technical.utils.FinalReqAggregationStrategy;
import esb.flows.technical.utils.ManagerRequestHelper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

public class SendFinalRequest extends RouteBuilder {

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2);

    @Override
    public void configure() throws Exception{
        from(AGGREG_TRAVELREQUEST)
            .routeId("aggre-final-request")
            .routeDescription("Concatenation des 3 demandes vol, car et hotel")
            .aggregate(constant(true), new FinalReqAggregationStrategy())
                .completionPredicate(SendFinalRequest::finish)
            .log("Aggregation des 3 requêtes en une TravelAgencyRequest : " + body().toString())
            .to(REQUETE_QUEUE)

        ;

        from(REQUETE_QUEUE)
            .routeId("manager-final-request")
            .routeDescription("Envoi de la demand eau manager")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader("Content-Type", constant("application/json"))
            .setHeader("Accept", constant("application/json"))
            .bean(ManagerRequestHelper.class, "buildSimpleRequest(${body}")
                .log("Transformation de la TravelAgencyRequest en .wsdl pour le service d'acceptation de voyage " + body().toString())
            .inOut(MANAGER_REQUEST_ENDPOINT)
            .process(response2String)
            .log("Reception dans la boîte mail du manager : " + body().toString())
            .to(EMAIL_MANAGER + "?fileName=mailManager" + "${header[requete-id]}" + ".txt")
        ;

        from(FILE_INPUT_MANAGER)
            .routeId("manager-file-answer")
            .unmarshal(CsvFormat.buildCsvFormat())
              .split(body())
              .parallelProcessing().executorService(WORKERS)
                 .process(csv2Manager)
                 .log("Transformation du csv en ManagerAnswer : " + body().toString())
            .to(ANSWER_MANAGER)
        ;

        from(ANSWER_MANAGER)
            .routeId("manager-envoi-reponse")
            .routeDescription("envoit de la reponse du manager")
            .bean(ManagerRequestHelper.class, "buildSimpleAnswer(${body})")
                .log("Transformation de la ManagerAnswer en .wsdl pour le service de réponse : " + body().toString())
            .inOut(MANAGER_ANSWER_ENDPOINT)
            .process(response2String)
                .log("Reception dans la boîte mail de l'employé : " + body().toString())
            .to(EMAIL_EMPLOYE + "?fileName=mailEmploye" + "${header[requete-id]}"+".txt")
        ;
    }

    private static Processor csv2Manager = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        ManagerAnswer m =  new ManagerAnswer();
        m.setReponse((String) data.get("answer"));
        exchange.getIn().setBody(m);
        exchange.getIn().setHeader("requete-id", (String) data.get("id"));
    };

    private static boolean finish(Exchange exc){
        TravelAgencyRequest tr = (TravelAgencyRequest) exc.getIn().getBody();
        if(tr.getCarReq() != null && tr.getFlightReq() != null && tr.getHotelReq() != null){
            return true;
        }
        return false;
    }

    private static Processor response2String = (Exchange exchange) -> {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String rep = exchange.getIn().getBody(String.class);
        InputSource input = new InputSource(new StringReader(rep));
        String reponseStr = xpath.evaluate("//result", input);
        exchange.getIn().setBody(reponseStr);

    };
}
