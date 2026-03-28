package ru.jobtracker.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.jobtracker.auth_service.service.IntrospectionService;

@RestController
@RequiredArgsConstructor
public class IntrospectionController {

  private final IntrospectionService introspectionService;

  @PostMapping("/oauth2/introspect")
  public ResponseEntity<?> introspect(
      @RequestParam("token") String token,
      @RequestParam(value = "client_id", required = false) String clientId,
      @RequestParam(value = "client_secret", required = false) String clientSecret) {
    return ResponseEntity.ok(introspectionService.introspect(token, clientId, clientSecret));
  }
}
