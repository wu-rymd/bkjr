package com.ase.restservice.jwt;


import java.util.Date;
import com.ase.restservice.model.Client;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
  private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

  @Value("${app.jwt.secret}")
  private String secretKey;

  /**
   * Generates access token based on client and date.
   * @param client
   * @return jwt
   */
  public String generateAccessToken(Client client) {
    return Jwts.builder()
            .setSubject(String.format("%s", client.getClientId()))
            .setIssuer("Kaiserscmarnn")
            .claim("role", client.getRole())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();

  }
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

  /**
   * checks if the given token is valid.
   * @param token
   * @return boolean of weather it is valid
   */
  public boolean validateAccessToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
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

  /**
   * gets subject hidden in token.
   * @param token
   * @return subject.
   */
  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  /**
   * gets role of the token owner hidden in token.
   * @param token
   * @return role
   */
  public String getRole(String token) {
    return (String) parseClaims(token).get("role");
  }

  /**
   * parses claims based on the token.
   * @param token
   * @return parsed jwt
   */
  private Claims parseClaims(String token) {
    return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
  }
}
