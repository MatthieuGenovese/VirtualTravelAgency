package esb.flows.technical.utils;

import esb.flows.technical.data.*;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.Random;

public class ManagerRequestHelper {


    private XPath xpath = XPathFactory.newInstance().newXPath();

    public String buildSimpleRequest(TravelAgencyRequest travelRequest) {
        Car c = travelRequest.getCarReq();
        Hotel h = travelRequest.getHotelReq();
        Flight f = travelRequest.getFlightReq();
        StringBuilder builder = new StringBuilder();
        builder.append("<cook:answer xmlns:cook=\"http://informatique.polytech.unice.fr/soa1/cookbook/\">\n");
        builder.append("  <request>\n");
        builder.append("    <address_hostel>" + h.getDestination() + "</address_hostel>\n");
        builder.append("    <departure_date_plane>" + f.getDate() + "</departure_date_plane>\n");
        builder.append("    <departure_place_plane>" + "0" + "</departure_place_plane>\n");
        builder.append("    <destination_place_plane>" + f.getDestination() + "</destination_place_plane>\n");
        builder.append("    <end_date_car>"+ c.getDate() + "</end_date_car>\n");
        builder.append("    <end_date_hostel>" + "21/01/1203" + "</end_date_hostel>\n");
        builder.append("    <id>" + "0" + "</id>\n");
        builder.append("    <model_car>"+ "Peugeot" + "</model_car>\n");
        builder.append("    <price_car>" + c.getPrice() +"</price_car>\n");
        builder.append("    <price_hostel>" + h.getPrice() + "</price_hostel>\n");
        builder.append("    <price_plane>"+ f.getPrice() + "</price_plane>\n");
        builder.append("    <seat_plane>" + "0" + "</seat_plane>\n");
        builder.append("    <start_date_car>" + c.getDate() + "</start_date_car>\n");
        builder.append("    <start_date_hostel>" + "0" + "</start_date_hostel>\n");
        builder.append("  </request>\n");
        builder.append("</cook:answer>\n");
        return builder.toString();
    }

    public String buildSimpleAnswer(ManagerAnswer m) {
        StringBuilder builder = new StringBuilder();
        builder.append("<cook:answer xmlns:cook=\"http://informatique.polytech.unice.fr/soa1/cookbook/\">\n");
        builder.append("  <request>\n");
        builder.append("    <choix>" +m.getReponse() + "</choix>\n");
        builder.append("    <messageTravel>" + "toto va à la plage" + "</messageTravel>\n");
        builder.append("  </request>\n");
        builder.append("</cook:answer>\n");
        return builder.toString();
    }

}
