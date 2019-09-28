package io.hashimati.offerservice.rest;


import io.hashimati.offerservice.domains.Offer;
import io.hashimati.offerservice.services.OfferServices;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.MediaType;

import javax.inject.Inject;

@Controller("/api")
public class OfferController {


    @Inject
    private OfferServices offerServices;


    @Get("/hello")
    public String hello()
    {

        return "Hello from offers"; 
    }

    @Post("/submit")
    public Single<Offer> saveRequest(@Body Offer offer)
    {
        System.out.println(offer);
        return offerServices.save(offer);

    }
}
