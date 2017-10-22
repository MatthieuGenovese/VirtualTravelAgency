package esb.flows.technical.utils;

public class Endpoints {

    // file entrées / sorties de camel
    public static final String FILE_INPUT_HOTEL    = "file:..?fileName=test2Hotel.csv";
    public static final String FILE_INPUT_DIRECTORY    = "file:..?fileName=test2Flight.csv";
    public static final String FILE_INPUT_CAR    = "file:..?fileName=test2Car.csv";
    public static final String CAMEL_OUTPUT_TESTA    = "file:..?fileName=resFlight.txt";
    public static final String CAMEL_OUTPUT_TESTB    = "file:..?fileName=resFlight2.txt";
    public static final String CAMEL_OUTPUT_TESTHOTEL    = "file:..?fileName=resHotel.txt";
    public static final String CAMEL_OUTPUT_CARFILE    = "file:..fileName=resCar.txt";
    public static final String LETTER_OUTPUT_DIR = "file:/servicemix/camel/output";

    // Internal message queues
    public static final String FLIGHT_QUEUE = "activemq:flight-queue";
    public static final String HOTEL_QUEUE = "activemq:hotel-queue";
    public static final String CAR_QUEUE = "activemq:car-queue";

    // Direct endpoints (flow modularity without a message queue overhead)
    public static final String RETRIEVE_A_FLIGHTA = "direct:retrieve-a-flighta";
    public static final String RETRIEVE_A_FLIGHTB = "direct:retrieve-a-flightb";
    public static final String RETRIEVE_A_HOTELA = "direct:retrieve-a-hotela";
    public static final String RETRIEVE_CAR_A = "direct:retrieve-car-a";
    public static final String COMPUTE_TAXES    = "direct:handle-a-citizen";

    // External partners
    public static final String FLIGHTSERVICE_ENDPOINTA  = "http://localhost:9080/flightreservation-service-document/registry";
    public static final String HOTELSERVICE_ENDPOINTA  = "http://localhost:9090/cars-hotels-reservation-service-rest/hotels";
    public static final String CARSERVICE_ENDPOINTA  = "http://localhost:9090/cars-hotels-reservation-service-rest/cars";
    public static final String FLIGHTSERVICE_ENDPOINTB = "http://localhost:9030/vols/webapi/vols";

    // Dead letters channel
    public static final String DEATH_POOL = "activemq:global:dead";
    public static final String BAD_CITIZEN = "activemq:badCitizens";

}