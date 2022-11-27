package com.ase.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that is thrown when a requested resource does not exist in the database.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with Throwable. Allows exception wrapping via Exception.
   */
  public ResourceNotFoundException(Throwable cause) {
    super(cause);
  }

  /**
   * Custom error for when client requests a resource that does not exist.
   * @param message error message
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
