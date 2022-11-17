package com.ase.restservice.jwt;


import java.util.Date;

import com.ase.restservice.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
  private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

  @Value("${app.jwt.secret}")
  private String SECRET_KEY;

  public String generateAccessToken(Account account) {
    return Jwts.builder()
            .setSubject(String.format("%s", account.getAccountId()))
            .setIssuer("Kaiserscmarnn")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();

  }
}