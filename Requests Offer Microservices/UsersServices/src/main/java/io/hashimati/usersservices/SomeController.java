
/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */
package io.hashimati.usersservices;


import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class SomeController {

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello()
    {

        return "Hello from Users Service";

    }
}
