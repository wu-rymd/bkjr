package com.ase.restservice.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.ase.restservice.model.Client;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtTokenUtil jwtUtil;

  /**
   * Guarentees single execution per request.
   * If the Authorization header of the request doesn’t contain a Bearer token,
   * it continues the filter chain without updating authentication context.
   * Else, if the token is not verified, continue the filter
   * chain without updating authentication context.
   * If the token is verified, update the authentication
   * context with the user details ID and email. In other words,
   * it tells Spring that the user is authenticated, and continue the downstream filters.
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    if (!hasAuthorizationBearer(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = getAccessToken(request);

    if (!jwtUtil.validateAccessToken(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    setAuthenticationContext(token, request);
    filterChain.doFilter(request, response);
  }

  /**
   * .
   * @param request
   * @return
   */
  private boolean hasAuthorizationBearer(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
      return false;
    }

    return true;
  }

  /**
   * Gets accesstoken from request.
   * @param request
   * @return
   */
  private String getAccessToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    String token = header.split(" ")[1].trim();
    return token;
  }

  /**
   * Sets context of authentication.
   * @param token
   * @param request
   */
  private void setAuthenticationContext(String token, HttpServletRequest request) {
    UserDetails userDetails = getUserDetails(token);

    UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
    );

    authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * Gets user details.
   * @param token
   * @return
   */
  private UserDetails getUserDetails(String token) {
    Client userDetails = new Client();

    Claims claims = jwtUtil.parseClaims(token);
    String subject = (String) claims.get(Claims.SUBJECT);
    String roles = (String) claims.get("role");
//    roles = roles.replace("[", "").replace("]", "");
//    String[] roleNames = roles.split(",");

//    for (String aRoleName : roleNames) {//TODO redundant because there is only one role
////      userDetails.addRole(new Role(aRoleName));
//      userDetails.setRole(aRoleName);
//    }
    userDetails.setRole(roles);

    //getting subject multiple times
    String[] jwtSubject = jwtUtil.getSubject(token).split(","); //no need for split

    userDetails.setClientId(jwtSubject[0]);
    //wtSubject only has name right now.
//    userDetails.setRole();
    //role is not filled
    return userDetails;
  }
}
