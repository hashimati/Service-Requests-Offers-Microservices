
package io.hashimati.usersservices.rest;

import javax.inject.Inject;

import io.hashimati.usersservices.constants.Roles;
import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

/**
 * UserController
 */

 @Controller("/")
public class UserController {

    @Inject
    UserRepository userRepository; 


    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post("/signup/{role}")
    public Single<String> signUp(@Body User user, @PathVariable(value = "role") String role) 
    {
    
        if(role.equals(Roles.USER) || role.equals(Roles.SERVICE_PROVIDER))  
        {
            user.setRoles(role);

            return userRepository.save(user) != null? Single.just("done!") : Single.just("failed"); 

        }

        return Single.just("Invalid Request"); 

        
    }
    


}