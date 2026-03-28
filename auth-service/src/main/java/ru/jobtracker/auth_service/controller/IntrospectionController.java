package ru.jobtracker.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.jobtracker.auth_service.dto.response.IntrospectResponse;
import ru.jobtracker.auth_service.service.IntrospectionService;

@RestController
@RequiredArgsConstructor
public class IntrospectionController {

  private final IntrospectionService oath2Service;

  @PostMapping("/oauth2/introspect")
  public ResponseEntity<IntrospectResponse> introspect(
      @RequestParam("token") String token,
      @AuthenticationPrincipal UserDetails client) {
    return ResponseEntity.ok(oath2Service.introspect(token, client));
  }
}
