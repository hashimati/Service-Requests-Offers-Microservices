package io.hashimati.usersservices;

import javax.inject.Inject;

import io.hashimati.usersservices.constants.Roles;
import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.hashimati.usersservices.security.BCPasswordEncoder;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

public class Application {

    public static void main(String[] args) {

        Micronaut.run(Application.class);
    }


    @Inject
    private UserRepository userRepository; 

    @Inject
    private BCPasswordEncoder bCPasswordEncoder; 
    @EventListener
    void init(StartupEvent startupEvent){
        
        User ahmed = new User("Ahmed",bCPasswordEncoder.encode("hello@1234")); 
        ahmed.setRoles(Roles.USER);

        User hashim = new User("Hashim", bCPasswordEncoder.encode("hello@1234")); 
        hashim.setRoles(Roles.SERVICE_PROVIDER);
        
        userRepository.save(ahmed); 
        userRepository.save(hashim); 

    }
}