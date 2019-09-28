
package io.hashimati.offerservice.clients;

import io.hashimati.offerservice.domains.Request;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

/**
 * RequestsClient
 */


 @Client(id="request-services", path = "/api")
public interface RequestsClient {

    @Get("/requests/{requestNo}")
    public Request findRequestByNo(@PathVariable(name ="requestNo" ) String requestNo); 

    
    
}