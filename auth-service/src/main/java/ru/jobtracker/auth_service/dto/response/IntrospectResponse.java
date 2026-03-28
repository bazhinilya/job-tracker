package ru.jobtracker.auth_service.dto.response;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record IntrospectResponse(
    // Обязательные поля по стандарту OAuth2 Introspection
    boolean active,

    // Опциональные поля с данными пользователя
    @JsonProperty("user_id") 
    UUID userId,

    String username,

    String email,

    Set<String> roles,

    // Стандартные OAuth2 поля
    /* Subject */
    UUID sub,

    /* Client ID */
    @JsonProperty("client_id") 
    String clientId,

    /* Token type */
    @JsonProperty("token_type") 
    String tokenType,

    /* Expiration time (seconds) */
    Long exp,

    /* Issued at (seconds) */
    Long iat) {

}
