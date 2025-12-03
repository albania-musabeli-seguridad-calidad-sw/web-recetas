package com.musabeli.frontrecetas.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginResponseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("Creation and field access")
    class CreationAndAccess {

        @Test
        void shouldCreateInstanceWithAllFields() {
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.x";
            String message = "Login successful";
            String username = "pepe@gmail.com";

            LoginResponse response = new LoginResponse(token, message, username);

            assertThat(response.token()).isEqualTo(token);
            assertThat(response.message()).isEqualTo(message);
            assertThat(response.username()).isEqualTo(username);
        }

        @Test
        void shouldAllowNullValues() {
            LoginResponse response = new LoginResponse(null, "Error", null);

            assertThat(response.token()).isNull();
            assertThat(response.message()).isEqualTo("Error");
            assertThat(response.username()).isNull();
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        void instancesWithSameValuesShouldBeEqual() {
            LoginResponse a = new LoginResponse("tok123", "OK", "user1");
            LoginResponse b = new LoginResponse("tok123", "OK", "user1");

            assertThat(a)
                    .isEqualTo(b)
                    .hasSameHashCodeAs(b);
        }

        @Test
        void instancesWithDifferentValuesShouldNotBeEqual() {
            LoginResponse a = new LoginResponse("tok1", "OK", "user");
            LoginResponse b = new LoginResponse("tok2", "OK", "user");
            LoginResponse c = new LoginResponse("tok1", "Error", "user");

            assertThat(a).isNotEqualTo(b);
            assertThat(a).isNotEqualTo(c);
        }
    }

    @Nested
    @DisplayName("toString behavior")
    class ToStringBehavior {

        @Test
        void shouldIncludeAllFieldsInToString() {
            LoginResponse response = new LoginResponse("abc123", "Welcome", "maria");

            assertThat(response.toString())
                    .contains("token=abc123")
                    .contains("message=Welcome")
                    .contains("username=maria");
        }

        @Test
        void shouldNotExposeTokenInPlainTextIfSecurityIsCritical() {
            // This is just a reminder test - records include everything by default
            LoginResponse response = new LoginResponse("super-secret-token", "OK", "user");

            assertThat(response.toString()).contains("super-secret-token");
            // If token logging is a security risk, consider customizing toString() in the future
        }
    }

    @Nested
    @DisplayName("JSON serialization and deserialization")
    class JsonSerialization {

        @Test
        void shouldSerializeToJsonCorrectly() throws JsonProcessingException {
            LoginResponse response = new LoginResponse(
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxx",
                    "Login successful",
                    "juan.perez"
            );

            String json = mapper.writeValueAsString(response);

            String expected = """
                    {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxx","message":"Login successful","username":"juan.perez"}
                    """.trim();

            assertThat(json).isEqualTo(expected);
        }

        @Test
        void shouldDeserializeFromJsonCorrectly() throws JsonProcessingException {
            String json = """
                    {
                      "token": "xyz789",
                      "message": "Bienvenido",
                      "username": "ana"
                    }
                    """;

            LoginResponse response = mapper.readValue(json, LoginResponse.class);

            assertThat(response.token()).isEqualTo("xyz789");
            assertThat(response.message()).isEqualTo("Bienvenido");
            assertThat(response.username()).isEqualTo("ana");
        }

        @Test
        void shouldHandleNullFieldsInJson() throws JsonProcessingException {
            String json = """
                    {"token": null, "message": "Error", "username": null}
                    """;

            LoginResponse response = mapper.readValue(json, LoginResponse.class);

            assertThat(response.token()).isNull();
            assertThat(response.message()).isEqualTo("Error");
            assertThat(response.username()).isNull();
        }
    }
}