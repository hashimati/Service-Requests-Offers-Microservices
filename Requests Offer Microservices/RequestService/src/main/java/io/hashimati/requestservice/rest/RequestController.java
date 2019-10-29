package io.hashimati.requestservice.rest; 


import java.security.Principal;

import javax.inject.Inject;

import io.hashimati.requestservice.clients.OffersClient;
import io.hashimati.requestservice.constants.Roles;
import io.hashimati.requestservice.domains.Offer;
import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.hashimati.requestservice.services.RequestServices;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.utils.SecurityService;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Controller("/api")
public class RequestController {


    @Inject
    private RequestServices requestServices;

    @Inject
    private OffersClient offersClient; 

    @Secured({Roles.USER})
    @Post("/submit")
    public Single<Request> saveRequest(@Body Request request, Principal principal )
    {
        request.setRequesterName(principal.getName());
        return requestServices.save(request);
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/{requestId}")
    public Single<Request> findRequestByNo(@PathVariable(value ="requestId" ) String requestId){



        return requestServices.findRequestByNo(requestId); 
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/getAll")
    public Flowable<Request> findAll(){

        return requestServices.findAll(); 
    }
    


    @Secured({Roles.USER})
    @Get("/requests/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId, Principal principal, @Header("Authorization") String authentication){
    
        


        return offersClient.rejectOffer(requestId, offerId, authentication); 

    }
    @Get("/requests/accept/{requestId}/{offerId}")
    public Single<String> acceptOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId, @Header("Authorization") String authentication)
    {
        Single<String> acceptingOfferMessage =  offersClient.acceptOffer(requestId, offerId, authentication);
        
        if(acceptingOfferMessage.blockingGet().toLowerCase().contains("success"))
        {

            return requestServices.takeAction(requestId, RequestStatus.DONE); 
        }
        return Single.just("failed"); 

    }
    @Get("/offers/{requestNo}")
    public Flowable<Offer> getOffers(@PathVariable("requestNo") String requestNo,Principal principal,  @Header("Authorization") String authentication)
    {

        if(principal.getName().toLowerCase().equals
        (requestNo.substring(0, requestNo.indexOf("_")))){
           
            return offersClient.findOffersByRequestNo(requestNo, authentication); 

        }
        return Flowable.just(null); 
    }


}
