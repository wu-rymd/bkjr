package com.ase.restservice.jwt;


import java.util.Date;

import com.ase.restservice.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
  private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

  @Value("${app.jwt.secret}")
  private String SECRET_KEY;

  public String generateAccessToken(Account account) {
    return Jwts.builder()//TODO BUG HERE I think
            .setSubject(String.format("%s", account.getAccountId()))
            .setIssuer("Kaiserscmarnn")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();

  }
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

  public boolean validateAccessToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException ex) {
      LOGGER.error("JWT expired", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
    } catch (MalformedJwtException ex) {
      LOGGER.error("JWT is invalid", ex);
    } catch (UnsupportedJwtException ex) {
      LOGGER.error("JWT is not supported", ex);
    } catch (SignatureException ex) {
      LOGGER.error("Signature validation failed");
    }

    return false;
  }

  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();
  }
}