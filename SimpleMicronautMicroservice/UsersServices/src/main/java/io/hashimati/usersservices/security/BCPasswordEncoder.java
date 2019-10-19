package io.hashimati.usersservices.security;

import javax.inject.Singleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.micronaut.security.authentication.providers.PasswordEncoder;


/**
 * BCPasswordEncoder
 */

 @Singleton
public class BCPasswordEncoder implements PasswordEncoder{
    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();
    
    @Override
    public String encode(String rawPassword) {
        // TODO Auto-generated method stub
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        // TODO Auto-generated method stub
        return delegate.matches(rawPassword, encodedPassword);
    }



    
}