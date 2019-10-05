
package io.hashimati.offerservice.clients;

import io.hashimati.offerservice.domains.Request;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;

/**
 * RequestsClient
 */


 @Client(id="request-services", path = "/api")
public interface RequestsClient {

    @Get("/requests/{requestId}")
    public Request findRequestByNo(@PathVariable(value ="requestId" ) String requestNo); 

    
    
}