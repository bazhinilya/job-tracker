package ru.jobtracker.auth_service.service;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.jobtracker.auth_service.dto.request.LoginRequest;
import ru.jobtracker.auth_service.dto.request.RegisterRequest;
import ru.jobtracker.auth_service.dto.response.LoginResponse;
import ru.jobtracker.auth_service.dto.response.RegisterResponse;
import ru.jobtracker.auth_service.model.TokenInfo;
import ru.jobtracker.auth_service.model.User;
import ru.jobtracker.auth_service.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public RegisterResponse register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new RuntimeException("Username already exists");
    }

    String encodedPassword = passwordEncoder.encode(request.password());
    User user = new User(request.username(), encodedPassword, request.email());

    User createdUser = userRepository.save(user);
    log.info("User '{}' was created", createdUser.getUsername());

    return new RegisterResponse(createdUser.getUsername(), createdUser.getEmail());
  }

  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findByUsername(request.username())
        .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    TokenInfo tokenInfo = tokenService.createToken(user);

    return new LoginResponse(
        tokenInfo.getToken(),
        tokenInfo.getExpiresAt() - tokenInfo.getCreatedAt(),
        "Bearer");
  }

  public Map<String, Object> logout(String token) {
    if (token == null || !token.startsWith("Bearer ")) {
      throw new RuntimeException("Token is not Bearer");
    }
    String extractedToken = token.substring(7);
    boolean isRevoked = tokenService.revokeToken(extractedToken);
    if (!isRevoked) {
      throw new RuntimeException("Token is not revoked");
    }
    return Map.of("message", "Logged out");
  }
}
