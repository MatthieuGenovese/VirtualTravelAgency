package esb.flows;

        import esb.flows.technical.data.*;
        import org.apache.camel.Exchange;
        import org.junit.Before;
        import org.junit.Test;

        import java.net.UnknownHostException;

        import static esb.flows.technical.utils.Endpoints.*;

public class DeathPoolTest extends ActiveMQTest {
    private String spendsCsv;

    //on initialise les requetes de tests
    @Before
    public void initRequests(){
        spendsCsv = "idGlobale,firstName,lastName,email,id,prix,reason,date,country,currency\n" +
                "1,momo,chennouf,mc154254@etu.unice.fr,01;02,45;98,resto;avion,28/06/2006;28/01/2017,AT;AT,EUR;EUR";;
    }

    @Override
    public String isMockEndpointsAndSkip() {
        return SPENDSERVICE_ENDPOINT + "|" + DEATH_POOL
                ;
    }

    //on définie ici les endpoints à tester
    @Override
    public String isMockEndpoints() {
        return FILE_INPUT_SPEND
                ;
    }

    //on déifinie ici les reponses automatiques des services non testé
    @Before
    public void initMocks() {
        resetMocks();

        mock(SPENDSERVICE_ENDPOINT).whenAnyExchangeReceived((Exchange exc) -> {
            exc.setException(new UnknownHostException());
        });


    }

    //On vérifie que le context d'execution est bien mocké
    @Test
    public void testExecutionContext() throws Exception {
        isAvailableAndMocked(DEATH_POOL);
        assertNotNull(context.hasEndpoint(FILE_INPUT_SPEND));
//        isAvailableAndMocked(FILE_INPUT_SPEND);
        isAvailableAndMocked(SPENDSERVICE_ENDPOINT);
    }

    @Test
    public void testSpendsRouteError() throws Exception {

        mock(DEATH_POOL).expectedMessageCount(1);
        mock(FILE_INPUT_SPEND).expectedMessageCount(1);
        mock(SPENDSERVICE_ENDPOINT).expectedMessageCount(1);
        template.sendBody(FILE_INPUT_SPEND, spendsCsv);
        mock(DEATH_POOL).assertIsSatisfied();
    }
}
