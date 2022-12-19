package com.ase.restservice.controller;

import javax.validation.Valid;

import com.ase.restservice.auth.AuthRequest;
import com.ase.restservice.auth.AuthResponse;
import com.ase.restservice.jwt.JwtTokenUtil;
import com.ase.restservice.model.Client;
import com.ase.restservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  @Autowired
  private ClientRepository clientRepository;

  /**
   * Signup URI for authentication.
   * @param request
   * @return responds with body or unauthorized status
   */
  @PostMapping("/auth/signup")
  public ResponseEntity<?> signup(@RequestBody @Valid AuthRequest request) {
    // https://stackoverflow.com/questions/26587082/http-status-code-for-username-
    // already-exists-when-registering-new-account
    //not ideal to call repository directly
    //maybe we can use customUser Details Service
    if (clientRepository.findClientId(request.getClientId()).isPresent()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
              .body("This client_id is already being used.");
    }
    Client newClient = new Client();
    newClient.setClientId(request.getClientId());
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    newClient.setPassword(passwordEncoder.encode(request.getPassword()));
    newClient.setRole("USER");
    clientRepository.save(newClient);

    return login(request);

  }
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
   * @return clientId
   */
  @GetMapping("/auth/client")
  public ResponseEntity<?> getClientId() {
    String clientId = getUsernameOfClientLogged();
    return ResponseEntity.ok().body(clientId);
  }
}
