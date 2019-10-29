package io.hashimati.usersservices.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
/**
 * BCPasswordEncoder
 * @author Ahmed Al Hashmi @Hashimati
 *
 */
 
 @Factory
 public class BCPasswordEncoder{    
    @Prototype
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}