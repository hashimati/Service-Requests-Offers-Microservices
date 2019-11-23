package io.hashimati.requestservice.rest; 


import java.security.Principal;
import java.util.HashMap;

import javax.inject.Inject;

import io.hashimati.requestservice.clients.OffersClient;
import io.hashimati.requestservice.constants.Roles;
import io.hashimati.requestservice.domains.Offer;
import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.hashimati.requestservice.services.RequestService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Controller("/api")
public class RequestController {


    @Inject
    private RequestService requestService;

    @Inject
    private OffersClient offersClient; 

    @Secured({Roles.USER})
    @Post("/submit")
    public Single<Request> saveRequest(@Body Request request, Principal principal )
    {
        request.setRequesterName(principal.getName());
        return requestService.save(request);
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/{requestId}")
    public Single<Request> findRequestByNo(@PathVariable(value ="requestId" ) String requestId){

        return requestService.findRequestByNo(requestId); 
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/getAll")
    public Flowable<Request> findAll(){

        return requestService.findAll(); 
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/getRequestIn{city}")
    public Flowable<Request> findByCity(@PathVariable("city") String city){
        return requestService.findByCity(city); 
    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Post("/requests/getRequestNearToMe")
    public Flowable<Request> findNearBy(@Body HashMap<String,Double> location){
        if(location.containsKey("longitude") && location.containsKey("latitude"))
               return requestService.findNearBy(location);
              
         else 
            return Flowable.just(null); 
    }

    
    @Secured({Roles.USER})
    @Get("/requests/")
    public Flowable<Request> findAll(Principal principal){
        return requestService.findAll(principal.getName()); 

    }
    
 
    @Secured({Roles.USER})
    @Get("/requests/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId, Principal principal, @Header("Authorization") String authorization){
            return offersClient.rejectOffer(requestId, offerId, authorization); 
    }
    
    @Secured({Roles.USER})
    @Get("/requests/accept/{requestId}/{offerId}")
    public Single<String> acceptOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId,Principal principal,  @Header("Authorization") String authorization)
    {
 
            Single<String> acceptingOfferMessage =  offersClient.acceptOffer(requestId, offerId, authorization);

            if(acceptingOfferMessage.blockingGet().toLowerCase().contains("success"))
            {

                return requestService.takeAction(requestId, RequestStatus.DONE); 
            }
            else 
                return Single.just("failed"); 
       
    }
    
    @Secured({Roles.USER})
    @Get("/offers/{requestNo}")
    public Flowable<Offer> getOffers(@PathVariable("requestNo") String requestNo,Principal principal,  @Header("Authorization") String authentication)
    {



        if(principal.getName().toLowerCase().equalsIgnoreCase(requestNo.substring(0, requestNo.indexOf("_")))){         
            return offersClient.findOffersByRequestNo(requestNo, authentication); 
        }
        return Flowable.just(null); 
    }


}
