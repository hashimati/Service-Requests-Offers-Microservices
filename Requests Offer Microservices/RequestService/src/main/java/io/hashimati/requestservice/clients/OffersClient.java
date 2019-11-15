package io.hashimati.requestservice.clients;

import java.util.List;

import io.hashimati.requestservice.domains.Offer;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * OffersClient
 */


@Client(id="offers-services", path = "/api")
public interface OffersClient {

    @Get("/offers/request/{requestId}")
    @CircuitBreaker(reset = "30s",attempts = "2")
    public Flowable<Offer> findOffersByRequestNo(@PathVariable(value="requestId") String requestId, @Header("Authorization") String authorization); 

    @Get("/offers/offer/{offerNo}")
    @CircuitBreaker(reset = "30s",attempts = "2")
    public Single<Offer> findOfferById(@PathVariable(value = "offerNo") String offerNo, @Header("Authorization") String authorization);

    //  @Put("/offers/update/{offerNo}")
    // public Single<Offer> updateOfferStatus(@PathVariable(name="offerNo") String offerNo, @Body OfferStatus offerStatus);

    
    @CircuitBreaker(reset = "30s",attempts = "2")
    @Get("/offers/reject/{requestId}/{offerId}")
    public Single<String> rejectOffer(@PathVariable(name = "requestId") String requestId,
     @PathVariable(name = "offerId") String offerId, @Header("Authorization") String authorization);

     @CircuitBreaker(reset = "30s",attempts = "2")
     @Get("/offers/accept/{requestId}/{offerId}")
	public Single<String> acceptOffer(@PathVariable(name = "requestId") String requestId,
    @PathVariable(name = "offerId") String offerId, @Header("Authorization") String authorization);
    
}