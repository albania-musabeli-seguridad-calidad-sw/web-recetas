package com.musabeli.frontrecetas.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecetaController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "pepe")
    void home_conUsuarioLogueado() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "pepe");

        mockMvc.perform(get("/").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recientes"))
                .andExpect(model().attributeExists("populares"))
                .andExpect(model().attribute("currentUser", "pepe"));
    }

    @Test
    @WithMockUser(username = "maria")
    void buscar_conUsuarioLogueado() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "maria");

        mockMvc.perform(get("/buscar").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("resultados"))
                .andExpect(model().attribute("currentUser", "maria"));
    }


    @Test
    void detalle_recetaExistente() throws Exception {
        mockMvc.perform(get("/receta/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle-receta"))
                .andExpect(model().attributeExists("receta"))
                .andExpect(model().attribute("receta", Matchers.notNullValue()))
                .andExpect(model().attribute("currentUser", Matchers.nullValue()));
    }

    @Test
    @WithMockUser(username = "juan")
    void detalle_recetaExistente_conUsuarioLogueado() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        mockMvc.perform(get("/receta/4").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("detalle-receta"))
                .andExpect(model().attributeExists("receta"))
                .andExpect(model().attribute("currentUser", "juan"));
    }


    @Test
    void detalle_recetaNoExistente_redirigeAHome() throws Exception {
        mockMvc.perform(get("/receta/99"))
                .andExpect(status().isFound()) // 302
                .andExpect(redirectedUrl("/"));
    }


    @Test
    void detalle_otrasRecetas_coberturaExtra() throws Exception {
        mockMvc.perform(get("/receta/2")).andExpect(status().isOk()).andExpect(view().name("detalle-receta"));
        mockMvc.perform(get("/receta/3")).andExpect(status().isOk()).andExpect(view().name("detalle-receta"));
        mockMvc.perform(get("/receta/5")).andExpect(status().isOk()).andExpect(view().name("detalle-receta"));
        mockMvc.perform(get("/receta/6")).andExpect(status().isOk()).andExpect(view().name("detalle-receta"));
        mockMvc.perform(get("/receta/7")).andExpect(status().isOk()).andExpect(view().name("detalle-receta"));
    }
}