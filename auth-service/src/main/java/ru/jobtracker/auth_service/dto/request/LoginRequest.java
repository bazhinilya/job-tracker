package ru.jobtracker.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Имя пользователя обязательно") 
    String username,

    @NotBlank(message = "Пароль обязателен") 
    String password) {
}
