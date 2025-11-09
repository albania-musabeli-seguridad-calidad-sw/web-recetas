package com.musabeli.frontrecetas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class UserDetailsServiceImpl {

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> User.withUsername(username)
        .password("")
        .authorities("ROLE_USER")
        .build();
    }
}
