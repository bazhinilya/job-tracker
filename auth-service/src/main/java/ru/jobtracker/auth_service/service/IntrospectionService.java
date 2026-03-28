package ru.jobtracker.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import ru.jobtracker.auth_service.dto.response.IntrospectResponse;
import ru.jobtracker.auth_service.model.TokenInfo;

@Service
@RequiredArgsConstructor
public class IntrospectionService {

  private final TokenService tokenService;

  public IntrospectResponse introspect(String token, UserDetails client) {
    boolean hasIntrospectScope = client.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("SCOPE_introspect"));

    if (!hasIntrospectScope) {
      throw new RuntimeException("Introspect is denied");
    }

    if (!StringUtils.hasText(token)) {
      throw new RuntimeException("Token is not Bearer");
    }

    TokenInfo tokenInfo = tokenService.introspectToken(token);
    if (tokenInfo == null || !tokenInfo.isActive()) {
      throw new RuntimeException("Invalid token");
    }

    return IntrospectResponse.builder()
        .active(tokenInfo.isActive())
        .userId(tokenInfo.getUserId())
        .username(tokenInfo.getUsername())
        .email(tokenInfo.getEmail())
        .roles(tokenInfo.getRoles())
        .sub(tokenInfo.getUserId())
        .clientId("auth-server")
        .tokenType("Bearer")
        .exp(tokenInfo.getExpiresAt() / 1000)
        .iat(tokenInfo.getCreatedAt() / 1000)
        .build();
  }
}
