package io.hashimati.usersservices.security; 

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.reactivestreams.Publisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.hashimati.usersservices.domains.User;
import io.hashimati.usersservices.repository.UserRepository;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.Flowable;

/**
 * @author Ahmed Al Hashmi @Hashimati
 *
 */

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider  {

    @Inject
    private UserRepository userRepository;

    @Inject 
    private PasswordEncoder PasswordEncoder; 


    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {

        //if User is not exist return Authentication Failed
        if(!userRepository.existsByUsername(authenticationRequest.getIdentity().toString())){
            
            return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)); 
        }


        User user = userRepository.findUserByUsername(authenticationRequest.getIdentity().toString());  
        
    
        if ( PasswordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            return Flowable.just(new UserDetails(user.getUsername(),
                    Arrays.asList(user.getRoles()
                            .replace(" ", "")
                            .split(","))));
        }
        
        return Flowable.just(new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)); 
    }
}