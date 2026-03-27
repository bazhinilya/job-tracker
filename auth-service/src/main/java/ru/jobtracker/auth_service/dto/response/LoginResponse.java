package ru.jobtracker.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
    /* JWT токен для доступа */
    @JsonProperty("access_token") String accessToken,

    /* Сколько секунд живет токен */
    @JsonProperty("expires_in") long expiresIn,

    /* Всегда "Bearer" */
    @JsonProperty("token_type") String tokenType) {
}
