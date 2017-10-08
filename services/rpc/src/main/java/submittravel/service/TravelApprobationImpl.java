package submittravel.service;

import submittravel.data.ApprobationAnswer;
import submittravel.data.ApprobationRequest;
import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;

import javax.jws.WebService;

@WebService(targetNamespace   = "http://informatique.polytech.unice.fr/soa1/cookbook/",
        portName          = "ExternalApprobationTravelPort",
        serviceName       = "ExternalApprobationTravelService",
        endpointInterface = "submittravel.service.TravelApprobationService")
public class TravelApprobationImpl implements TravelApprobationService {


    public ApprobationAnswer answer(ApprobationRequest request) {
        return buildSubmit(request);
    }


    public ApprobationAnswer buildSubmit(ApprobationRequest request){
        ApprobationAnswer result = new ApprobationAnswer();
        String message ="";
        if(request.getChoix()== true){
            message = message + "Your request is validate \n";
        }else{
            message = message + "Your request is not validate \n";
        }
        message =message + "\n"+request.getMessageTravel();
        result.setResultmessage(message);
        return result;
    }


}


