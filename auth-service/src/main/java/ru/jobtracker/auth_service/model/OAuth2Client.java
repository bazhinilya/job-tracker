package ru.jobtracker.auth_service.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "oauth2_clients")
public class OAuth2Client {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String clientId;

  @Column(nullable = false)
  private String clientSecret;

  @Column(nullable = false)
  private String clientName;

  private String scopes;

  public Collection<GrantedAuthority> getAuthorities() {
    if (!StringUtils.hasText(scopes)) {
      return Arrays.asList();
    }
    return Arrays.stream(scopes.split(" "))
        .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
        .collect(Collectors.toList());
  }
}
