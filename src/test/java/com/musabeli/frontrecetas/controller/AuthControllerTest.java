package com.musabeli.frontrecetas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void showLoginForm_devuelveVistaLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


    @Test
    void processLogin_errorCredenciales_devuelveLoginConMensajeError() throws Exception {
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(Class.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/login")
                        .param("username", "pepe")
                        .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Error del servidor"))
                .andExpect(request().sessionAttributeDoesNotExist("username"));
    }

    @Test
    void processLogin_errorServidor_devuelveLoginConErrorGenerico() throws Exception {
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                any(Class.class)
        )).thenThrow(new RuntimeException("Connection refused"));

        mockMvc.perform(post("/login")
                        .param("username", "pepe")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Error del servidor"))
                .andExpect(request().sessionAttributeDoesNotExist("username"));
    }

    @Test
    void logout_invalidaSesionYRedirigeALoginConParametroLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
    }
}