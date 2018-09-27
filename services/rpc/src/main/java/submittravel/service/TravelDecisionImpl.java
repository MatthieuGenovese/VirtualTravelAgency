package submittravel.service;


import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;

import javax.jws.WebService;

@WebService(targetNamespace   = "http://informatique.polytech.unice.fr/soa1/cookbook/",
        portName          = "ExternalSubmitTravelPort",
        serviceName       = "ExternalSubmitTravelService",
        endpointInterface = "submittravel.service.TravelDecisionService")
public class TravelDecisionImpl implements TravelDecisionService{


    public TravelAnswer answer(TravelRequest request) {
        return buildSubmit(request);
    }


    public TravelAnswer buildSubmit(TravelRequest request){
        TravelAnswer result = new TravelAnswer();
        String message ="Submit : "+request.getIdemploye()+": \n";
        message = message +"CarTicket{\n  start date:" + request.getStart_date_car()+ "  end date:" + request.getEnd_date_car()+ "  model:"+request.getModel_car()+"  price:"+request.getPrice_car()+"}\n";
        message = message + "HostelTicket{\n address:" + request.getAddress_hostel() + "  start date:" + request.getStart_date_hostel()+ " end_date:"+request.getEnd_date_hostel()+"  price:"+request.getPrice_hostel()+"}\n";
        message = message + "PlaneTicket{\n  departure date:" + request.getDeparture_date_plane()+ "  departure place:"+request.getDeparture_place_plane()+"  destination place:"+request.getDestination_place_plane()+"  seat:"+request.getSeat_plane()+"  price:"+request.getPrice_car()+"}\n";

        result.setIdentifier("Id : "+request.getIdemploye());
        result.setAnswer(message);
        return result;
    }
}

