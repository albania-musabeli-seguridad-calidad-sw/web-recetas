package com.musabeli.frontrecetas.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginRequestTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Nested
    @DisplayName("Creation and field access")
    class CreationAndAccess {

        @Test
        void shouldCreateInstanceWithCorrectValues() {

            String username = "pepe@gmail.com";
            String password = "secret123";

            LoginRequest request = new LoginRequest(username, password);
            
            assertThat(request.username()).isEqualTo(username);
            assertThat(request.password()).isEqualTo(password);
        }

        @Test
        void shouldHaveCorrectToStringRepresentation() {
            LoginRequest request = new LoginRequest("juan", "pass123");

            assertThat(request.toString())
                    .contains("username=juan")
                    .contains("password=pass123");
        }
    }

    @Nested
    @DisplayName("equals and hashCode")
    class EqualsAndHashCode {

        @Test
        void instancesWithSameValuesShouldBeEqual() {
            LoginRequest a = new LoginRequest("ana", "123456");
            LoginRequest b = new LoginRequest("ana", "123456");

            assertThat(a)
                    .isEqualTo(b)
                    .hasSameHashCodeAs(b);
        }

        @Test
        void instancesWithDifferentValuesShouldNotBeEqual() {
            LoginRequest a = new LoginRequest("ana", "123456");
            LoginRequest b = new LoginRequest("ana", "otroPass");
            LoginRequest c = new LoginRequest("otroUser", "123456");

            assertThat(a).isNotEqualTo(b);
            assertThat(a).isNotEqualTo(c);
            assertThat(a.hashCode()).isNotEqualTo(b.hashCode());
        }

        @Test
        void shouldBeEqualToItself() {
            LoginRequest request = new LoginRequest("test", "test");
            assertThat(request).isEqualTo(request);
        }
    }

    @Nested
    @DisplayName("JSON serialization and deserialization with Jackson")
    class JsonSerialization {

        @Test
        void shouldSerializeCorrectlyUsingJsonProperty() throws JsonProcessingException {
            LoginRequest request = new LoginRequest("maria", "myPass");

            String json = mapper.writeValueAsString(request);

            String expectedJson = """
                    {"username":"maria","password":"myPass"}
                    """.trim();

            assertThat(json).isEqualTo(expectedJson);
        }

        @Test
        void shouldDeserializeCorrectlyFromJson() throws JsonProcessingException {
            String json = """
                    {"username":"carlos","password":"securePass"}
                    """;

            LoginRequest request = mapper.readValue(json, LoginRequest.class);

            assertThat(request.username()).isEqualTo("carlos");
            assertThat(request.password()).isEqualTo("securePass");
        }

        @Test
        void shouldFailWhenDeserializingMissingRequiredField() {
            String jsonWithoutUsername = """
                    {"password":"123"}
                    """;

            assertThatThrownBy(() -> mapper.readValue(jsonWithoutUsername, LoginRequest.class))
                    .isInstanceOf(JsonProcessingException.class)
                    .hasMessageContaining("Missing required creator property 'username'");
        }
    }
}