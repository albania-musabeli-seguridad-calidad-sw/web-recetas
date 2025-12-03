package com.musabeli.frontrecetas.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginRequest(
        @JsonProperty(value = "username", required = true) String username,
        @JsonProperty(value = "password", required = true) String password
) {
    @JsonCreator
    public LoginRequest {}
}