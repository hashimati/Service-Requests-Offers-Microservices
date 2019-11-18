
/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */

package io.hashimati.usersservices.services;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jasypt.util.password.StrongPasswordEncryptor;

import io.hashimati.usersservices.constants.Roles;
import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
@Singleton
public class UsersService {

    
    @Inject
    private StrongPasswordEncryptor strongPasswordEncryptor ;
    @Inject
    private UserRepository userRepository; 

    
    @EventListener
    void init(StartupEvent startupEvent){
        
        User ahmed = new User("Ahmed",strongPasswordEncryptor.encryptPassword("hello@1234")); 
        ahmed.setRoles(Roles.USER);

        User hashim = new User("Hashim", strongPasswordEncryptor.encryptPassword("hello@1234")); 
        hashim.setRoles(Roles.SERVICE_PROVIDER);
  
        if(!userRepository.existsByUsername(ahmed.getUsername()))
            userRepository.save(ahmed); 
        if(!userRepository.existsByUsername(hashim.getUsername()))    
            userRepository.save(hashim); 

    }

}