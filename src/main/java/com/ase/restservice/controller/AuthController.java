package com.ase.restservice.controller;

import javax.validation.Valid;

import com.ase.restservice.auth.AuthRequest;
import com.ase.restservice.auth.AuthResponse;
import com.ase.restservice.jwt.JwtTokenUtil;
import com.ase.restservice.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ase.restservice.ApplicationSecurity.getUsernameOfClientLogged;

@RestController
public class AuthController {
  @Autowired
  private AuthenticationManager authManager;
  @Autowired
  private JwtTokenUtil jwtUtil;

  /**
   * Login URI for authentication.
   * @param request
   * @return responds with body or unauthorized status
   */
  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
    try {
      Authentication authentication = authManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getClientId(), request.getPassword())
      );

      Client client = (Client) authentication.getPrincipal();
      String accessToken = jwtUtil.generateAccessToken(client);
      AuthResponse response = new AuthResponse(client.getClientId(), accessToken);

      return ResponseEntity.ok().body(response);

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  /**
   * Gets the logged in client.
   * @param request
   * @return clientId
   */
  @GetMapping("/auth/client")
  public ResponseEntity<?> getClientId() {
    String clientId = getUsernameOfClientLogged();
    return ResponseEntity.ok().body(clientId);
  }
}
