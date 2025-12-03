package com.musabeli.frontrecetas.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JwtSessionFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpSession session;

    @InjectMocks
    private JwtSessionFilter jwtSessionFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext(); // Limpiamos el contexto de seguridad para cada test
    }

    @Test
    void doFilterInternal_sinSesion_continuaSinSetearAutenticacion() throws ServletException, IOException {
        // GIVEN: No hay sesión
        when(request.getSession(false)).thenReturn(null);

        // WHEN
        jwtSessionFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userDetailsService);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    void doFilterInternal_conUsernameEnSesion_seteaAutenticacionYContinua() throws ServletException, IOException {
        // GIVEN: Hay sesión con username
        String username = "testuser";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // WHEN
        jwtSessionFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService).loadUserByUsername(username);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }



    @Test
    void doFilterInternal_conUsernameEnSesion_seteaAutenticacionCorrecta() throws ServletException, IOException {
        // GIVEN: Hay sesión con username
        String username = "testuser";
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("username")).thenReturn(username);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // WHEN
        jwtSessionFilter.doFilterInternal(request, response, filterChain);

        // THEN
        verify(filterChain).doFilter(request, response);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, auth);
        assertEquals(userDetails, auth.getPrincipal());
        assertNull(auth.getCredentials()); // Null, como en el código
        assertEquals(userDetails.getAuthorities(), auth.getAuthorities());
        assertTrue(auth.isAuthenticated()); // Debe estar autenticado
    }

}
