
/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */

package io.hashimati.usersservices.services;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.hashimati.usersservices.repository.UserRepository;

@Singleton
public class UsersService {

    
    @Inject
    private PasswordEncoder passwordEncoder; 
    @Inject
    private UserRepository userRepository; 


}