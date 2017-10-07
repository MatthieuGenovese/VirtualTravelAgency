package submittravel.service;


import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;

import javax.jws.WebService;

@WebService(targetNamespace   = "http://informatique.polytech.unice.fr/soa1/cookbook/",
        portName          = "ExternalSubmitTravelPort",
        serviceName       = "ExternalSubmitTravelService",
        endpointInterface = "submittravel.service.TravelDecisionService")
public class TravelDecisionImpl implements TravelDecisionService{


    public TravelAnswer answer(TravelRequest request,boolean answer) {
        String message = "";
        if(answer==true){
            message = "Your request has been accepted : \n";

        }else{
            message = "Sorry , but your request was rejected : \n";
        }
        message = message + request.toString();

        return buildResponse(request.getIdemploye(),message);
    }


    public TravelAnswer buildResponse(String id, String answer){
        TravelAnswer result = new TravelAnswer();
        result.setIdentifier(id);
        result.setAnswer(answer);
        return result;
    }
}

