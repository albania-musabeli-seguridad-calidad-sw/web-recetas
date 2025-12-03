package com.musabeli.frontrecetas.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsServiceImplTest {

    @Test
    void userDetailsService_deberiaRetornarUserConUsernameCorrecto() {
        // GIVEN
        UserDetailsServiceImpl config = new UserDetailsServiceImpl();
        UserDetailsService service = config.userDetailsService();
        String usernameEsperado = "testuser";

        // WHEN
        UserDetails user = service.loadUserByUsername(usernameEsperado);

        // THEN
        assertNotNull(user);
        assertEquals(usernameEsperado, user.getUsername());
    }


    @Test
    void userDetailsService_deberiaRetornarUserConRoleUser() {
        // arrange
        UserDetailsServiceImpl config = new UserDetailsServiceImpl();
        UserDetailsService service = config.userDetailsService();
        String username = "testuser";

        // act
        UserDetails user = service.loadUserByUsername(username);

        // assert
        assertTrue(user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }



}
