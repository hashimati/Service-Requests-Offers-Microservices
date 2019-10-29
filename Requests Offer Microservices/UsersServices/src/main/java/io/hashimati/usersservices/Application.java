
/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */
package io.hashimati.usersservices;

import javax.inject.Inject;

import io.hashimati.usersservices.constants.Roles;
import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder; 


public class Application {

    public static void main(String[] args) {

        Micronaut.run(Application.class);
    }


    @Inject
    private UserRepository userRepository; 

    @Inject
    private PasswordEncoder passwordEncoder; 
    @EventListener
    void init(StartupEvent startupEvent){
        
        User ahmed = new User("Ahmed",passwordEncoder.encode("hello@1234")); 
        ahmed.setRoles(Roles.USER);

        User hashim = new User("Hashim", passwordEncoder.encode("hello@1234")); 
        hashim.setRoles(Roles.SERVICE_PROVIDER);
  
        if(!userRepository.existsByUsername(ahmed.getUsername()))
            userRepository.save(ahmed); 
        if(!userRepository.existsByUsername(hashim.getUsername()))    
            userRepository.save(hashim); 

    }
}