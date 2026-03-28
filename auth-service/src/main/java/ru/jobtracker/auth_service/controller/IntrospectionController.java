package ru.jobtracker.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.jobtracker.auth_service.dto.response.IntrospectResponse;
import ru.jobtracker.auth_service.service.Oath2Service;

@RestController
@RequiredArgsConstructor
public class IntrospectionController {

  private final Oath2Service oath2Service;

  @PostMapping("/oauth2/introspect")
  public ResponseEntity<IntrospectResponse> introspect(
      @RequestParam("token") String token,
      @RequestParam(value = "client_id", required = false) String clientId,
      @RequestParam(value = "client_secret", required = false) String clientSecret) {
    return ResponseEntity.ok(oath2Service.introspect(token, clientId, clientSecret));
  }
}
