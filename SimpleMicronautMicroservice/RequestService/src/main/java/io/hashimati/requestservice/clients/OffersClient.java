package io.hashimati.requestservice.clients;

import java.util.List;

import io.hashimati.requestservice.domains.Offer;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

/**
 * OffersClient
 */


@Client(id="offers-services", path = "/api")
public interface OffersClient {

    @Get("/offers/request/{requestId}")
    public Single<List<Offer>> findOffersByRequestNo(@PathVariable(value="requestId") String requestId); 

    @Get("/offers/offer/{offerNo}")
    public Single<Offer> findOfferById(@PathVariable(value = "offerNo") String offerNo);

    //  @Put("/offers/update/{offerNo}")
    // public Single<Offer> updateOfferStatus(@PathVariable(name="offerNo") String offerNo, @Body OfferStatus offerStatus);


    @Get("/offers/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(name = "requestId") String requestId,
     @PathVariable(name = "offerId") String offerId);


     @Get("/offers/accept/{requestId}/{offerId}")
	public Single<String> acceptOffer(@PathVariable(name = "requestId") String requestId,
    @PathVariable(name = "offerId") String offerId);
    
}