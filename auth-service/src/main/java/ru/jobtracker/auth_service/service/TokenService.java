package ru.jobtracker.auth_service.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.jobtracker.auth_service.model.TokenInfo;
import ru.jobtracker.auth_service.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

  private final RedisTemplate<String, Object> redisTemplate;

  @Value("${auth.token.access-token-ttl}")
  private Long accessTokenTtl;

  private static final String TOKEN_KEY_PREFIX = "auth:token:";

  /** Создает opaque токен */
  public TokenInfo createToken(User user) {
    String opaqueToken = UUID.randomUUID().toString().replace("-", "");
    TokenInfo tokenInfo = TokenInfo.fromUser(user, opaqueToken, accessTokenTtl);

    String tokenKey = TOKEN_KEY_PREFIX + opaqueToken;
    redisTemplate.opsForValue().set(
        tokenKey,
        tokenInfo,
        accessTokenTtl,
        TimeUnit.MILLISECONDS);

    log.info("Created token for user: {}", user.getUsername());
    return tokenInfo;
  }

  /** Валидирует токен для introspection */
  public TokenInfo introspectToken(String token) {
    String tokenKey = TOKEN_KEY_PREFIX + token;
    TokenInfo tokenInfo = (TokenInfo) redisTemplate.opsForValue().get(tokenKey);

    if (tokenInfo == null) {
      log.warn("Token not found: {}", token);
      return null;
    }

    if (tokenInfo.isExpired()) {
      log.warn("Token expired: {}", token);
      revokeToken(token);
      return null;
    }

    return tokenInfo;
  }

  /** Отзывает токен */
  public boolean revokeToken(String token) {
    String tokenKey = TOKEN_KEY_PREFIX + token;
    Boolean deleted = redisTemplate.delete(tokenKey);

    if (Boolean.TRUE.equals(deleted)) {
      log.info("Revoked token: {}", token);
      return true;
    }
    return false;
  }
}
