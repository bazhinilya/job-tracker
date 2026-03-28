package ru.jobtracker.auth_service.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.jobtracker.auth_service.model.OAuth2Client;
import ru.jobtracker.auth_service.repository.OAuth2ClientRepository;

@Service
@RequiredArgsConstructor
public class OAuth2ClientDetailsService implements UserDetailsService {

  private final OAuth2ClientRepository clientRepository;

  @Override
  public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
    OAuth2Client client = clientRepository.findByClientId(clientId)
        .orElseThrow(() -> new UsernameNotFoundException("Client not found: " + clientId));

    return User.builder()
        .username(client.getClientId())
        .password(client.getClientSecret())
        .authorities(client.getAuthorities())
        .build();
  }

}
