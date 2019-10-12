package io.hashimati.offerservice.rest;

import java.security.Principal;
import javax.inject.Inject;

import io.hashimati.offerservice.clients.RequestsClient;
import io.hashimati.offerservice.constants.Roles;
import io.hashimati.offerservice.domains.Offer;
import io.hashimati.offerservice.domains.Request;
import io.hashimati.offerservice.domains.enums.OfferStatus;
import io.hashimati.offerservice.services.OfferServices;
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
public class OfferController {
    @Inject
    private OfferServices offerServices;

    @Inject
    private RequestsClient requestsClient; 

    @Get("/hello")
    public String hello()
    {
        return "Hello from offers"; 
    }

    @Secured({Roles.SERVICE_PROVIDER})
    @Post("/submit")
    public Single<Offer> saveRequest(@Body Offer offer, Principal principal,  @Header("Authorization") String authentication)
    {
        offer.setBy(principal.getName());   
        return offerServices.save(offer, authentication);
    }

    @Secured({Roles.SERVICE_PROVIDER})
    @Get("/requests/get")
    public Flowable<Request> findAll(@Header("Authorization") String authentication){
        return requestsClient.findAll(authentication); 

    }

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/offers/{requestId}")
    public Flowable<Offer> findOffersByRequestNo(@PathVariable(value = "requestId") String requestId)
    {
        return offerServices.findOffersByRequestNo(requestId); 
    }

    @Secured({Roles.USER})
    @Get("/offers/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId, Principal principal)
    {
        return offerServices.takeAction(requestId, offerId, OfferStatus.REJECTED, principal.getName()); 
    }
    
    @Secured({Roles.USER})
     @Get("/offers/accept/{requestId}/{offerId}")
	public Single<String> acceptOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId, Principal principal){
        return offerServices.takeAction(requestId, offerId, OfferStatus.ACCEPTED, principal.getName()); 

    }
}