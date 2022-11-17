package com.ase.restservice.controller;

import javax.validation.Valid;

import com.ase.restservice.auth.AuthRequest;
import com.ase.restservice.auth.AuthResponse;
import com.ase.restservice.jwt.JwtTokenUtil;
import com.ase.restservice.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthApi {
  @Autowired AuthenticationManager authManager;
  @Autowired
  JwtTokenUtil jwtUtil;

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
    try {
      Authentication authentication = authManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getAccountId(), request.getPassword())
      );

      Account account = (Account) authentication.getPrincipal();
      String accessToken = jwtUtil.generateAccessToken(account);
      AuthResponse response = new AuthResponse(account.getAccountId(), accessToken);

      return ResponseEntity.ok().body(response);

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
