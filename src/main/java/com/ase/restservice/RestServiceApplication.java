package com.ase.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * REST API.
 */
@SpringBootApplication
class RestServiceApplication {

  /**
   * Entry point to application.
   * @param args args
   */
  public static void main(String[] args) {

    SpringApplication.run(RestServiceApplication.class, args);
  }

}
