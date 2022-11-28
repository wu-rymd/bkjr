package com.ase.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that is thrown when a requested resource already exists in
 * the database.
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with Throwable. Allows exception wrapping via Exception.
   */
  public ResourceAlreadyExistsException(Throwable cause) {
    super(cause);
  }

  /**
   * Custom error for when client tries to create a resource that already exists.
   *
   * @param message error message
   */
  public ResourceAlreadyExistsException(String message) {
    super(message);
  }
}
