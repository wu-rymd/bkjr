package com.ase.restservice;

import com.ase.restservice.jwt.JwtTokenFilter;
import com.ase.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;


@Configuration
public class ApplicationSecurity {


  private static final String[] AUTH_WHITELIST = {
          // -- Swagger UI v2
          "/v2/api-docs",
          "/swagger-resources",
          "/swagger-resources/**",
          "/configuration/ui",
          "/configuration/security",
          "/swagger-ui.html",
          "/webjars/**",
          // -- Swagger UI v3 (OpenAPI)
          "/v3/api-docs/**",
          "/swagger-ui/**"
          // other public endpoints of your API may be appended to this array
  };


  @Resource
  private UserDetailsService userDetailsService;  //wot


  @Autowired
  private AccountRepository accountRepo;
  @Autowired
  private JwtTokenFilter jwtTokenFilter;


  @Bean //here
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
//    http.authorizeRequests().anyRequest().permitAll();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests()
            .antMatchers("/auth/login")
            .permitAll()
            .antMatchers("/v2/api-docs", "/configuration/ui",
                    "/swagger-resources/**", "/configuration/**", "/swagger-ui.html"
                    , "/webjars/**", "/csrf", "/")
            .permitAll()
            .anyRequest().authenticated();

    http.exceptionHandling()
            .authenticationEntryPoint(
                    (request, response, ex) -> {
                      response.sendError(
                              HttpServletResponse.SC_UNAUTHORIZED,
                              ex.getMessage()
                      );
                    }
            );

    http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    http.authenticationProvider(authenticationProvider());


    return http.build();
  }



  @Bean //new
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }


  @Bean //old but can stay
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
    return authConfiguration.getAuthenticationManager();
  }
}
