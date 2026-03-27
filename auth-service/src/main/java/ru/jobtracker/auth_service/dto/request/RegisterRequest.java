package ru.jobtracker.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "Имя пользователя обязательно") 
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов") 
    String username,

    @NotBlank(message = "Пароль обязателен") 
    @Size(min = 6, message = "Пароль должен быть минимум 6 символов") 
    String password,

    String email) {
}
