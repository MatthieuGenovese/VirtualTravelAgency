package esb.flows.technical.utils;

public class Endpoints {

    // file entrées / sorties de camel
    public static final String FILE_INPUT_HOTEL    = "file:/servicemix/camel/input?fileName=test2Hotel.csv";
    public static final String FILE_INPUT_FLIGHT = "file:/servicemix/camel/input?fileName=test2Flight.csv";
    public static final String FILE_INPUT_CAR    = "file:/servicemix/camel/input?fileName=test2Car.csv";
    public static final String FILE_INPUT_MANAGER = "file:/servicemix/camel/input?fileName=test2Manager.csv";
    public static final String FILE_INPUT_SPEND = "file:/servicemix/camel/input?fileName=test2Spend.csv";
    public static final String FILE_INPUT_SPEND_MANAGER = "file:/servicemix/camel/input?fileName=test2SpendManager.csv";
    public static final String EMAIL_EMPLOYE = "file:/servicemix/camel/output";
    public static final String EMAIL_MANAGER = "file:/servicemix/camel/output";

    // Internal message queues
    public static final String FLIGHT_QUEUE = "activemq:flight-queue";
    public static final String HOTEL_QUEUE = "activemq:hotel-queue";
    public static final String CAR_QUEUE = "activemq:car-queue";
    public static final String REQUETE_QUEUE = "activemq:requete-manager-queue";
    public static final String SPEND_QUEUE = "activemq:spend-queue";

    // Direct endpoints (flow modularity without a message queue overhead)
    public static final String RETRIEVE_A_FLIGHTA = "direct:retrieve-a-flighta";
    public static final String RETRIEVE_A_FLIGHTB = "direct:retrieve-a-flightb";
    public static final String RETRIEVE_A_HOTELA = "direct:retrieve-a-hotela";
    public static final String ANSWER_MANAGER = "direct:answer-route-manager";
    public static final String RETRIEVE_A_HOTELB = "direct:retrieve-a-hotelb";
    public static final String AGGREG_FLIGHT = "direct:agg-flight";
    public static final String AGGREG_HOTEL = "direct:agg-hotel";
    public static final String AGGREG_CAR = "direct:agg-car";
    public static final String RETRIEVE_CAR_A = "direct:retrieve-car-a";
    public static final String RETRIEVE_CAR_B = "direct:retrieve-car-b";
    public static final String AGGREG_TRAVELREQUEST = "direct:aggreg-final-req";

    // External partners
    public static final String FLIGHTSERVICE_ENDPOINTA  = "http:flightreservation:8080/flightreservation-service-document/registry";
    public static final String SPENDSERVICE_ENDPOINT = "http:spends:8080/vtg-spends/spends";
    public static final String HOTELSERVICE_ENDPOINTA  = "http:cars-hotels-reservation:8080/cars-hotels-reservation-service-rest/hotels";
    public static final String HOTELSERVICE_ENDPOINTB  = "http:hotels-team1:8080/tcs-hotel-service/hotels";
    public static final String CARSERVICE_ENDPOINTA  = "http:cars-hotels-reservation:8080/cars-hotels-reservation-service-rest/cars";
    public static final String CARSERVICE_ENDPOINTB = "http:travel-cars:9060/cars";
    public static final String FLIGHTSERVICE_ENDPOINTB = "http:vols-document:8080/vols/webapi/vols";
    public static final String MANAGER_REQUEST_ENDPOINT = "spring-ws:http://submittravel:8080/submittravel-service-rpc/ExternalSubmitTravelService";
    public static final String MANAGER_ANSWER_ENDPOINT = "spring-ws:http://submittravel:8080/submittravel-service-rpc/ExternalApprobationTravelService";
    // Dead letters channel
    public static final String DEATH_POOL = "activemq:global:dead";

}
