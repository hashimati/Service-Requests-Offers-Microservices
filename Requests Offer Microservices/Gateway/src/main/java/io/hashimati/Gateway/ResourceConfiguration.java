package io.hashimati.Gateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * ResourceConfiguration
 */

 @Configuration
 @EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter
{
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
          .antMatchers("/uaa/**")
          .permitAll()
          .antMatchers("/login")
          .permitAll()
          .antMatchers("/**")
      .authenticated();
    }
}



    
