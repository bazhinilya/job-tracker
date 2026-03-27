package ru.jobtracker.auth_service.model;

import java.util.Set;
import java.util.UUID;

public record TokenInfo (
    String token,
    UUID userId,
    String username,
    String email,
    Set<String> roles,
    Long createdAt,
    Long expiresAt,
    boolean active) {

  public static TokenInfo fromUser(User user, String token, Long ttl) {
    long now = System.currentTimeMillis();
    return new TokenInfo(
        token,
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles(),
        now,
        now + ttl,
        true);
  }

  public boolean isExpired() {
    return System.currentTimeMillis() > expiresAt;
  }
}
