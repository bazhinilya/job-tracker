package ru.jobtracker.auth_service.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenInfo {
  private String token;
  private UUID userId;
  private String username;
  private String email;
  private Set<String> roles;
  private Long createdAt;
  private Long expiresAt;
  private boolean active;

  public static TokenInfo fromUser(User user, String token, Long ttl) {
    long now = System.currentTimeMillis();
    Set<String> rolesCopy = new HashSet<>(user.getRoles());
    return TokenInfo.builder()
        .token(token)
        .userId(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .roles(rolesCopy)
        .createdAt(now)
        .expiresAt(now + ttl)
        .active(true)
        .build();

  }

  public boolean isExpired() {
    return System.currentTimeMillis() > expiresAt;
  }
}
