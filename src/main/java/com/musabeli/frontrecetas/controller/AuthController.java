package com.musabeli.frontrecetas.controller;

import com.musabeli.frontrecetas.dto.LoginRequest;
import com.musabeli.frontrecetas.dto.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.url}")
    private String apiUrl;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response,
            HttpSession session,
            Model model) {

        try {
            LoginRequest request = new LoginRequest(username, password);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<LoginResponse> apiResponse = restTemplate.exchange(
                    apiUrl + "/login",
                    HttpMethod.POST,
                    entity,
                    LoginResponse.class);
            
            
            String jwt = apiResponse.getBody().token().replace("Bearer ", "");
            System.out.println("JWT recibido (largo: " + jwt.length() + "): " + jwt.substring(0, 50) + "...");

            // guardar el nombre de usuario en la sesion
            session.setAttribute("username", username);
            
            return "redirect:/";

        } catch (HttpClientErrorException e) {
            System.out.println("Error API: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            model.addAttribute("error", "Error del servidor");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
    
}
