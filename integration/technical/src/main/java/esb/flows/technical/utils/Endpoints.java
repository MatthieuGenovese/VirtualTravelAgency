package esb.flows.technical.utils;

public class Endpoints {

    // file inputs
    public static final String FILE_INPUT_DIRECTORY    = "file:/home/matthieu?fileName=test2Flight.csv";
    public static final String CAMEL_OUTPUT_TEST    = "file:/home/matthieu?fileName=resFlight.txt";
    // file outputs
    public static final String LETTER_OUTPUT_DIR = "file:/servicemix/camel/output";

    // Internal message queues
    public static final String RETRIEVE_A_FLIGHT = "activemq:retrieve-a-flight";

    // Direct endpoints (flow modularity without a message queue overhead)
    public static final String COMPUTE_TAXES    = "direct:handle-a-citizen";

    // External partners
    public static final String FLIGHTSERVICE_ENDPOINT = "http://localhost:9080/flightreservation-service-document/registry";

    // Dead letters channel
    public static final String DEATH_POOL = "activemq:global:dead";
    public static final String BAD_CITIZEN = "activemq:badCitizens";

}
