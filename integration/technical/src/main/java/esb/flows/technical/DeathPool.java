package esb.flows.technical;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.net.UnknownHostException;
import static esb.flows.technical.utils.Endpoints.DEATH_POOL;
import static esb.flows.technical.utils.Endpoints.EMAIL_EMPLOYE;

public class DeathPool extends RouteBuilder{
    static int REDELIVERIES = 2;

    @Override
    public void configure() throws Exception {

        /*from(DEAD_SPEND)
                .log("Bad information for citizen ")
                .log(body().toString())
                .multicast()
                .to(EMAIL_EMPLOYE + "?fileName=errorSendEvidence.txt", DEATH_POOL)
        ;*/

        from(DEATH_POOL)
                .choice()
                    .when(header("err").isEqualTo("aggreg3"))
                        .to(EMAIL_EMPLOYE + "?fileName=errorAskingReservations.txt")
                    .when(header("err").isEqualTo("requete"))
                        .to(EMAIL_EMPLOYE + "?fileName=errorAskManagerService.txt")
                    .when(header("err").isEqualTo("spend"))
                        .to(EMAIL_EMPLOYE + "?fileName=errorSendEvidences.txt")
                    .when(header("err").isEqualTo("spendmanager"))
                        .to(EMAIL_EMPLOYE + "?fileName=errorAcceptRefuseRefund.txt")
                    .when(header("err").isEqualTo("voiturenotfound"))
                        .process(voiture)
                        .to(EMAIL_EMPLOYE + "?fileName=voiturenotfound.txt")
                    .when(header("err").isEqualTo("hotelnotfound"))
                        .process(hotel)
                        .to(EMAIL_EMPLOYE + "?fileName=hotelnotfound.txt")
                    .when(header("err").isEqualTo("failinput"))
                        .process(input)
                        .to(EMAIL_EMPLOYE + "?fileName=failinput.txt")
                    .when(header("err").isEqualTo("flightnotfound"))
                        .process(avion)
                        .to(EMAIL_EMPLOYE + "?fileName=flightnotfound.txt")
                .endChoice()
        ;


    }
    private static Processor input  = (Exchange exchange) -> {
        exchange.getIn().setBody("Erreur dans l'entrée utilisateur\n");
    };
    private static Processor avion = (Exchange exchange) -> {
        exchange.getIn().setBody("Aucun avion n'a été trouvé !\n");

    };

    private static Processor voiture = (Exchange exchange) -> {
        exchange.getIn().setBody("Aucune voiture n'a été trouvé !\n");
    };

    private static Processor hotel = (Exchange exchange) -> {
        exchange.getIn().setBody("Aucun hotel n'a été trouvé !\n");
    };
}
