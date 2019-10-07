package io.hashimati.offerservice.rest;


import java.util.List;

import javax.inject.Inject;

import io.hashimati.offerservice.constants.Roles;
import io.hashimati.offerservice.domains.Offer;
import io.hashimati.offerservice.domains.enums.OfferStatus;
import io.hashimati.offerservice.services.OfferServices;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.reactivex.Single;

@Controller("/api")
public class OfferController {
    @Inject
    private OfferServices offerServices;


    @Get("/hello")
    public String hello()
    {

        return "Hello from offers"; 
    }

    @Secured({Roles.USER})
    @Post("/submit")
    public Single<Offer> saveRequest(@Body Offer offer)
    {
        System.out.println(offer);
        return offerServices.save(offer);

    }
    @Get("/offers/{requestId}")
    public Single<List<Offer>> findOffersByRequestNo(@PathVariable(value = "requestId") String requestId)
    {
        return offerServices.findOffersByRequestNo(requestId); 
    }


    @Get("/offers/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId){

        return offerServices.takeAction(requestId, offerId, OfferStatus.REJECTED); 
    }


     @Get("/offers/accept/{requestId}/{offerId}")
	public Single<String> acceptOffer(@PathVariable(value = "requestId") String requestId, @PathVariable(value = "offerId") String offerId){
        return offerServices.takeAction(requestId, offerId, OfferStatus.ACCEPTED); 

    }
}
