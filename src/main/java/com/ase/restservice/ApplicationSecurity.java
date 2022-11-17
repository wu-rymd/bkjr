package com.ase.restservice;

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

import javax.annotation.Resource;

@Configuration
public class ApplicationSecurity {


  @Resource
  private UserDetailsService userDetailsService;  //wot
//  @Autowired
//  private UserDetailsService userDetailsService;

  @Autowired
  private AccountRepository accountRepo;


//  @Override //old
//  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable();
//    http.authorizeRequests().anyRequest().permitAll();
//    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.authorizeRequests().anyRequest().permitAll();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // http....;
    http.authenticationProvider(authenticationProvider());

    return http.build();
  }

//  @Override //old
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(
//            username -> userRepo.findByEmail(username)
//                    .orElseThrow(
//                            () -> new UsernameNotFoundException("User " + username + " not found.")));
//  }

  @Bean //new
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

//    Account account = accountRepo
//    UserDetailsService userDetailsService = UserDetailsService(
//
//      username -> accountRepo.findByEmail(username)
//              .orElseThrow(
//                      () -> new UsernameNotFoundException("User " + username + " not found."))
//
//    );

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }


  @Bean //old but can stay
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Override //old
//  @Bean
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
    return authConfiguration.getAuthenticationManager();
  }

}
