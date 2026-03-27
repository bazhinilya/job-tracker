package ru.jobtracker.auth_service.config;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.jobtracker.auth_service.model.TokenInfo;
import ru.jobtracker.auth_service.service.TokenService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

  private final TokenService tokenService;

  @Override
  public OAuth2AuthenticatedPrincipal introspect(String token) {
    TokenInfo tokenInfo = tokenService.introspectToken(token);

    if (tokenInfo == null) {
      log.warn("Invalid token: {}", maskToken(token));
      throw new RuntimeException("Invalid token");
    }

    Map<String, Object> attributes = Map.of(
        "user_id", tokenInfo.getUserId(),
        "username", tokenInfo.getUsername(),
        "email", tokenInfo.getEmail(),
        "roles", tokenInfo.getRoles(),
        "token", tokenInfo.getToken());

    Collection<GrantedAuthority> authorities = tokenInfo.getRoles().stream()
        .<GrantedAuthority>map(SimpleGrantedAuthority::new)
        .toList();

    return new OAuth2IntrospectionAuthenticatedPrincipal(attributes, authorities);
  }

  private String maskToken(String token) {
    return (token == null || token.length() < 8) ? "***"
        : token.substring(0, 4) + "..." + token.substring(token.length() - 4);
  }
}
