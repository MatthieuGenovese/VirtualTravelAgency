package esb.flows.technical;

import esb.flows.technical.data.Identity;
import esb.flows.technical.data.Price;
import esb.flows.technical.data.Spend;
import esb.flows.technical.data.SpendRequest;
import esb.flows.technical.utils.CsvFormat;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static esb.flows.technical.utils.Endpoints.*;

public class SubmitSpends extends RouteBuilder{

    private static final ExecutorService WORKERS = Executors.newFixedThreadPool(2);

    public void configure() {
        from(FILE_INPUT_SPEND)
                .onException(UnknownHostException.class).handled(true)
                .process(handleErr)
                .log("erreur capturée dans le service d'envoie des preuves au manager")
                .to(DEATH_POOL)
                // TODO: Pouvoir multicast le résultat dans la deathpool et ailleurs
                //.multicast()
                //.to(EMAIL_EMPLOYE + "?fileName=errorSendEvidence.txt", DEATH_POOL)
                .end()
                .routeId("refund-request")
                .routeDescription("demande de remboursement")
                .setHeader(Exchange.HTTP_METHOD, constant("POST")) // on choisis le type de requete (ici du POST en json)
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                    .split(body()) // on effectue un travaille en parralele sur la map >> on transforme tout ca en objet de type Flight
                    .parallelProcessing().executorService(WORKERS)
                    .process(csv2spendreq)
                    .log("transformation du csv en SpendRequest : " + body().toString())
                .marshal().json(JsonLibrary.Gson)
                .unmarshal().string()
                .process(spendreq2jsonservice)
                .log("transformation de la SpendRequest en json et envoi de la requête au service remboursement : " + body().toString())
                .inOut(SPENDSERVICE_ENDPOINT)
                .log("réponse du service : " + body().toString())
                .log(body().toString())
        ;


    }

    private static Processor spendreq2jsonservice = (Exchange exchange) -> {
        String recv = (String) exchange.getIn().getBody();
        String header = "{\"type\":\"submit\",\"spends\":";
        String endofReq = "}";
        exchange.getIn().setBody(header+recv+endofReq);

    };

    private static Processor handleErr = (Exchange exchange) -> {
        String rep = "The service is currently offline\n";
        exchange.getIn().setBody(rep);
    };


      /*idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency
1,momo,chennouf,mc154254@etu.unice.fr,01;02,2545;2758,resto;avion,28/01/2017;28/01/2017,France;France,EUR;EUR*/

    private static Processor csv2spendreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        ArrayList<Spend> spends =  new ArrayList<>();
        Identity i = new Identity();
        Price p = new Price();
        SpendRequest sr = new SpendRequest();
        String[] tmpId, tmpReason, tmpDate, tmpCountry, tmpPrice, tmpCurrency;
        tmpId = ((String) data.get("id")).split(";");
        tmpReason = ((String) data.get("reason")).split(";");
        tmpDate = ((String) data.get("date")).split(";");
        tmpCountry = ((String) data.get("country")).split(";");
        tmpPrice = ((String) data.get("prix")).split(";");
        tmpCurrency =((String) data.get("currency")).split(";");
        if(tmpId.length == 1){
            Spend s = new Spend();
            p.setCurrency((String) data.get("currency"));
            p.setPrice((String) data.get("prix"));
            s.setId((String) data.get("id"));
            s.setDate((String) data.get("date"));
            s.setCountry((String) data.get("country"));
            s.setPrice(p);
            s.setReason((String) data.get("reason"));
            spends.add(s);
        }
        else{
            for(int j = 0; j < tmpId.length; j++){
                Spend tmpSpend = new Spend();
                Price tmpPricegf = new Price();
                tmpSpend.setId(tmpId[j]);
                tmpSpend.setCountry(tmpCountry[j]);
                tmpSpend.setReason(tmpReason[j]);
                tmpSpend.setDate(tmpDate[j]);
                tmpPricegf.setPrice(tmpPrice[j]);
                tmpPricegf.setCurrency(tmpCurrency[j]);
                tmpSpend.setPrice(tmpPricegf);
                spends.add(tmpSpend);
            }
        }




        i.setEmail((String) data.get("email"));
        i.setFirstName((String) data.get("firstName"));
        i.setLastName((String) data.get("lastName"));
        sr.setId((String) data.get("idGlobale"));
        sr.setIdentity(i);
        sr.setSpend(spends);
        exchange.getIn().setBody(sr);
        //exchange.getIn().setHeader("requete-id", (String) data.get("id"));
    };


}
