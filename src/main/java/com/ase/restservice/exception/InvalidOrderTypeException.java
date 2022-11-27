package com.ase.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception that is thrown when a client tries to make a transaction of an invalid type.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOrderTypeException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with Throwable. Allows exception wrapping via Exception.
   */
  public InvalidOrderTypeException(Throwable cause) {
    super(cause);
  }

  /**
   * Custom error for when client tries to make a transaction of an invalid type.
   * @param message error message
   */
  public InvalidOrderTypeException(String message) {
    super(message);
  }
}
