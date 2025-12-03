package com.musabeli.frontrecetas.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import javax.crypto.SecretKey;


public class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey secretKey;


    private static final String SECRET_KEY_BASE64 = "ZnJhc2VzbGFyZ2FzcGFyYWNvbG9jYXJjb21vY2xhdmVlbnVucHJvamVjdG9kZWVtZXBsbG9hcmFqd3Rjb25zcHJpbmdzZWN1cml0eW1pcHJ1ZWJhZGVlam1wbG9wYXJhYmFzZTY0";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        // Generamos la clave exactamente igual que lo hace JwtService
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY_BASE64);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    @DisplayName("getUsername() debe extraer correctamente el subject del token")
    void getUsername_extraeSubjectCorrectamente() {
        // GIVEN
        String usernameEsperado = "juan@test.com";
        String token = generarTokenValido(usernameEsperado);

        // WHEN
        String usernameObtenido = jwtService.getUsername(token);

        // THEN
        assertEquals(usernameEsperado, usernameObtenido);
    }

    // Método auxiliar para generar tokens válidos (lo usaremos en todos los tests)
    private String generarTokenValido(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora
                .signWith(secretKey)
                .compact();
    }


    @Test
    @DisplayName("isTokenValid() debe retornar true con un token válido")
    void isTokenValid_conTokenValido_retornaTrue() {
        // GIVEN
        String token = generarTokenValido("test@user.com");

        // WHEN
        boolean esValido = jwtService.isTokenValid(token);

        // THEN
        assertTrue(esValido);
    }


    @Test
    @DisplayName("isTokenValid() debe retornar false con token mal formado")
    void isTokenValid_conTokenMalFormado_retornaFalse() {
        // GIVEN
        String tokenInvalido = "esto.no.es.un.jwt.valido";

        // WHEN
        boolean esValido = jwtService.isTokenValid(tokenInvalido);

        // THEN
        assertFalse(esValido);
    }


    @Test
    @DisplayName("isTokenValid() debe retornar false con token firmado con otra clave")
    void isTokenValid_conOtraClave_retornaFalse() {
        // GIVEN: Generamos una clave diferente
        SecretKey otraClave = Keys.hmacShaKeyFor("clave-diferente-para-prueba-1234567890".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        String tokenConOtraClave = Jwts.builder()
                .subject("user@test.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(otraClave)
                .compact();

        // WHEN
        boolean esValido = jwtService.isTokenValid(tokenConOtraClave);

        // THEN
        assertFalse(esValido);
    }


    @Test
    @DisplayName("isTokenValid() debe retornar false con token expirado")
    void isTokenValid_conTokenExpirado_retornaFalse() {
        // GIVEN: Token expirado (emitido y expirado en el pasado)
        String tokenExpirado = Jwts.builder()
                .subject("user@test.com")
                .issuedAt(new Date(System.currentTimeMillis() - 7200000)) // Emitido hace 2 horas
                .expiration(new Date(System.currentTimeMillis() - 3600000)) // Expiró hace 1 hora
                .signWith(secretKey)
                .compact();

        // WHEN
        boolean esValido = jwtService.isTokenValid(tokenExpirado);

        // THEN
        assertFalse(esValido);
    }


    @Test
    @DisplayName("isTokenValid() debe retornar false con token null, vacío o con espacios")
    void isTokenValid_conTokenNuloOVacio_retornaFalse() {
        assertAll(
                () -> assertFalse(jwtService.isTokenValid(null)),
                () -> assertFalse(jwtService.isTokenValid("")),
                () -> assertFalse(jwtService.isTokenValid("   "))
        );
    }

}