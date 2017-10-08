package submittravel.service;

import submittravel.data.TravelAnswer;
import submittravel.data.TravelRequest;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;


@WebService(name="TravelDecision", targetNamespace = "http://informatique.polytech.unice.fr/soa1/cookbook/")
public interface TravelDecisionService {

    @WebResult(name="result")
    TravelAnswer answer(@WebParam(name="request") TravelRequest request);

}
