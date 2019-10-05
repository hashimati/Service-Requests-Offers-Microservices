package io.hashimati.requestservice.rest; 


import javax.inject.Inject;

import io.hashimati.requestservice.clients.OffersClient;
import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.hashimati.requestservice.services.RequestServices;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;

@Controller("/api")
public class RequestController {


    @Inject
    private RequestServices requestServices;

    @Inject
    private OffersClient offersClient; 


    @Post("/submit")
    public Single<Request> saveRequest(@Body Request request)
    {
        System.out.println(request);
        return requestServices.save(request);
    }

    @Get("/requests/{requestId}")
    public Request findRequestByNo(@PathVariable(value ="requestId" ) String requestId){

        return requestServices.findRequestByNo(requestId); 
    }


    @Get("/requests/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId)
    {

        return offersClient.rejectOffer(requestId, offerId); 

    }
    @Get("/requests/accept/{requestId}/{offerId}")
    public Single<String> acceptOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId)
    {
        Single<String> acceptingOfferMessage =  offersClient.acceptOffer(requestId, offerId);
        
        if(acceptingOfferMessage.blockingGet().toLowerCase().contains("success"))
        {

            return requestServices.takeAction(requestId, RequestStatus.DONE); 
        }
        return Single.just("failed"); 

    }


}
