package esb.flows.technical;

import esb.flows.technical.data.*;
import esb.flows.technical.utils.CsvFormat;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import java.io.IOException;
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
                .onException(IOException.class).handled(true)
                    .process(handleErr)
                    .setHeader("err", constant("spend"))
                    .log("erreur capturée dans le service d'envoie des preuves au manager")
                    .to(DEATH_POOL)
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
                .choice()
                    .when(header("typereq").isNotEqualTo("retrieve"))
                        .marshal().json(JsonLibrary.Gson)
                        .log("transformation de la SpendRequest en json et envoi de la requête au service remboursement : " + body().toString())
                        .inOut(SPENDSERVICE_ENDPOINT)
                        .log("réponse du service : " + body().toString())
                        .log(body().toString())
                    .otherwise()
                        .log("transformation de la SpendRequest en json et envoi de la requête au service remboursement : " + body().toString())
                        .inOut(SPENDSERVICE_ENDPOINT)
                        .log("réponse du service : " + body().toString())
                        .log(body().toString())
                .endChoice()

        ;

        from(FILE_INPUT_SPEND_MANAGER)
                .onException(IOException.class).handled(true)
                    .process(handleErr)
                    .setHeader("err", constant("spendmanager"))
                    .log("erreur capturée dans le service d'envoie des preuves au manager")
                    .to(DEATH_POOL)
                .end()
                .routeId("refund-answer")
                .routeDescription("Validate ou invalidation de la demande de remboursement")
                .unmarshal(CsvFormat.buildCsvFormat())  // Body is now a List of Map<String -> Object>
                .split(body()) // on effectue un travaille en parralele sur la map >> on transforme tout ca en objet de type Flight
                .parallelProcessing().executorService(WORKERS)
                .process(csv2spendanswer)
                .log("transformation du csv en SpendAnswer : " + body().toString())
                .inOut(SPENDSERVICE_ENDPOINT)
                .log("réponse du service : " + body().toString())
                .log(body().toString())
        ;

    }

    private static Processor handleErr = (Exchange exchange) -> {
        String rep = "The service is currently offline\n";
        exchange.getIn().setBody(rep);
    };


      /*idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency
1,momo,chennouf,mc154254@etu.unice.fr,01;02,2545;2758,resto;avion,28/01/2017;28/01/2017,France;France,EUR;EUR*/
      //addSpend,1,momo,chennouf,mc154254@etu.unice.fr,03,98,resto,28/01/2017,AT,EUR

    private static Processor csv2spendanswer = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        String type = (String) data.get("type");
        String id = (String) data.get("id");
        String rep = "{\"type\":\"" + type + "\"" + ",\"id\":\"" + id + "\"}";
        exchange.getIn().setBody(rep);
    };

    private static Processor csv2spendreq = (Exchange exchange) -> {
        Map<String, Object> data = (Map<String, Object>) exchange.getIn().getBody();
        ArrayList<Spend> spends =  new ArrayList<>();
        Identity i = new Identity();
        Price p = new Price();
        SpendRequest sr = new SpendRequest();
        Bills b = new Bills();
        String[] tmpId, tmpReason, tmpDate, tmpCountry, tmpPrice, tmpCurrency;
        String type = ((String) data.get("type"));
        switch(type) {
            case "submit":
                tmpId = ((String) data.get("id")).split(";");
                tmpReason = ((String) data.get("reason")).split(";");
                tmpDate = ((String) data.get("date")).split(";");
                tmpCountry = ((String) data.get("country")).split(";");
                tmpPrice = ((String) data.get("prix")).split(";");
                tmpCurrency =((String) data.get("currency")).split(";");
                if (tmpId.length == 1) {
                    Spend s = new Spend();
                    p.setCurrency((String) data.get("currency"));
                    p.setPrice((String) data.get("prix"));
                    s.setId((String) data.get("id"));
                    s.setDate((String) data.get("date"));
                    s.setCountry((String) data.get("country"));
                    s.setPrice(p);
                    s.setReason((String) data.get("reason"));
                    spends.add(s);
                } else {
                    for (int j = 0; j < tmpId.length; j++) {
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
                b.setIdentity(i);
                b.setSpends(spends);
                b.setId((String) data.get("idGlobale"));
                sr.setType(type);
                sr.setBills(b);
                exchange.getIn().setBody(sr);
                exchange.getIn().setHeader("typereq","submit");
                break;
            case "addSpend":
                AddSpendRequest asp = new AddSpendRequest();
                Spend s = new Spend();
                p.setCurrency((String) data.get("currency"));
                p.setPrice((String) data.get("prix"));
                s.setId((String) data.get("id"));
                s.setDate((String) data.get("date"));
                s.setCountry((String) data.get("country"));
                s.setPrice(p);
                s.setReason((String) data.get("reason"));
                asp.setType(type);
                asp.setId((String) data.get("idGlobale"));
                asp.setSpend(s);
                exchange.getIn().setBody(asp);
                exchange.getIn().setHeader("typereq","addspend");
                break;
            case "retrieve":
                String res = "{\n" +
                        "      \"type\":\"retrieve\",\n" +
                        "      \"id\":"+"\""+(String) data.get("idGlobale")+"\"\n" +
                        "}";
                exchange.getIn().setHeader("typereq","retrieve");
                exchange.getIn().setBody(res);
                break;
            case "addJustification":
                Justification j = new Justification();
                j.setId((String) data.get("idGlobale"));
                j.setType(type);
                j.setJustification((String) data.get("justification"));
                exchange.getIn().setBody(j);
                break;
        }
        //exchange.getIn().setHeader("requete-id", (String) data.get("id"));
    };

   /*{
        "type":"submit",
        "bills":
        {
            "id":"1",
            "identity":
            {
                "firstName":"momo",
                "lastName":"chennouf",
                "email":"mc154254@etu.unice.fr"
            },
            "spends":
            [
                {
                "id":"01",
                "reason":"resto",
                "date":"28/06/2006",
                "country":"AT",
                "price":
                    {
                    "price":"45",
                    "currency":"EUR"
                    }
                },
                {
                "id":"02",
                "reason":"avion",
                "date":"28/01/2017",
                "country":"AT",
                "price":
                    {
                    "price":"98",
                    "currency":"EUR"
                    }
                }
            ]
        }
    }
    */

   /*{
 	"type":"submit",
 	"bills":
 	 {
 		"id":99,
 		"identity":
 		{
 			"firstName":"mohamed",
 			"lastName":"chennouf",
 			"email":"mohamed.chennouf@etu.unice"
 		},
 		"spends":
 		[
 			{
 			"id":"0",
 			"reason":"Restaurant",
 			"date":"05/02/2018",
 			"country": "AM" ,
 			"prix" :
 			    {
 				"price":120,
 				"currency":"EUR"
 				}
 			},
 			{
 			"id":"1",
 			"reason":"Restaurant",
 			"date":"01/02/2005",
 			"country": "AM" ,
 			"prix" :
 			    {
 				"price":90,
 				"currency":"EUR"
 				}
 			}
 		]
 	}
}*/


}
