package io.hashimati.usersservices.security;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.jasypt.util.password.StrongPasswordEncryptor;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Prototype;
/**
 * BCPasswordEncoder
 * @author Ahmed Al Hashmi @Hashimati
 *
 */
 
@Factory 
public class PasswordEncoder  {

    @Prototype
    public StrongPasswordEncryptor strongPasswordEncryptor(){
        return new StrongPasswordEncryptor(); 
    }
    



   
}