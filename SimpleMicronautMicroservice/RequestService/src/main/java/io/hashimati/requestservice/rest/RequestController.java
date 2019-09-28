package io.hashimati.requestservice.rest; 


import javax.inject.Inject;

import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.services.RequestServices;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.reactivex.Single;

@Controller("/api")
public class RequestController {


    @Inject
    private RequestServices requestServices;



    @Post("/submit")
    public Single<Request> saveRequest(@Body Request request)
    {
        System.out.println(request);
        return requestServices.save(request);
    }

    @Get("/requests/{requestNo}")
    public Request findRequestByNo(@PathVariable(name ="requestNo" ) String requestNo){

        return requestServices.findRequestByNo(requestNo); 
    }

}
