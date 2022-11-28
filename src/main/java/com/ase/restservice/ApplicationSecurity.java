package com.ase.restservice;

import com.ase.restservice.jwt.JwtTokenFilter;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

  @Resource
  private UserDetailsService userDetailsService;

  @Autowired
  private AccountRepository accountRepo;
  @Autowired
  private JwtTokenFilter jwtTokenFilter;

  @Value("${com.ase.restservice.ApplicationSecurity.production}")
  private Boolean production;
  /**
   * This method checks whether the account asked for
   * by the client is authorized to the client or not.
   * @param authentication authenticated client object
   * @param accountId accountId that client wants to open
   * @return whether client is allowed to access account or not
   */
  public boolean checkAccountId(Authentication authentication, String accountId) {
    // need to check which client this account has
    String clientName = authentication.getName();
    Account account =  accountRepo.findAccountByAccountId(accountId).orElseThrow(() ->
            new UsernameNotFoundException("Account Not Found with username: " + accountId)); //404

    return clientName.equals(account.getClientId()); //403
  }
  public static String getUsernameOfClientLogged() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else {
      return principal.toString();
    }
  }

  /**
   * This method checks whether a uri request should be granted based on authentication.
   * @param http the http request
   * @return allows http to be build to display
   * @throws Exception unauthorized
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    String[] accountIdWhiteList = {
            "/accounts/{accountId}/**",
            "/assets/{accountId}/**"
    };
    http.csrf().disable();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    if (production) {
      http.authorizeRequests()
              .antMatchers("/auth/login")
              .permitAll()
              .antMatchers(accountIdWhiteList)
              .access("@applicationSecurity.checkAccountId(authentication,#accountId)")
              .anyRequest().authenticated();
    } else {
      http.authorizeRequests().anyRequest().permitAll();
    }

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

  /**
   * Provides Authentication.
   * @return authentication with userdetailservice
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  /**
   * Returns password encoder object.
   * @return password encoder object
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   *
   * @param authConfiguration
   * @return authConfiguration
   * @throws Exception
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration)
          throws Exception {
    return authConfiguration.getAuthenticationManager();
  }
}
