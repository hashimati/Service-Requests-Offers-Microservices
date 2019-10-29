
package io.hashimati.offerservice.clients;

import io.hashimati.offerservice.constants.Roles;
import io.hashimati.offerservice.domains.Request;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * RequestsClient
 */


 @Client(id="request-services", path = "/api")
public interface RequestsClient {

    @Secured({Roles.SERVICE_PROVIDER, Roles.USER})
    @Get("/requests/{requestId}")
    public Single<Request> findRequestByNo(@PathVariable(value ="requestId" ) String requestNo, @Header("Authorization") String authentication); 

    @Get("/requests/get")
    public Flowable<Request> findAll(@Header("Authorization") String authentication); 
    

    
    
}