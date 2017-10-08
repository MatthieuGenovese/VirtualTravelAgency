package submittravel.service;

import submittravel.data.ApprobationAnswer;
import submittravel.data.ApprobationRequest;
import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name="TravelApprobation", targetNamespace = "http://informatique.polytech.unice.fr/soa1/cookbook/")
public interface TravelApprobationService {

    @WebResult(name="result")
    ApprobationAnswer answer(@WebParam(name="request") ApprobationRequest request);
}
