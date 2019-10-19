package io.hashimati.usersservices.security;

import javax.inject.Singleton;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



/**
 * BCPasswordEncoder
 */

 @Singleton
public class BCPasswordEncoder{
    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();
    
    
    public String encode(String rawPassword) {
        // TODO Auto-generated method stub
        return delegate.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        // TODO Auto-generated method stub
        return delegate.matches(rawPassword, encodedPassword);
    }



    
}