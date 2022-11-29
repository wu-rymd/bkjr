package com.ase.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for when the transaction requested is invalid (e.g. insufficient assets)
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with Throwable. Allows exception wrapping via Exception.
   */
  public InvalidTransactionException(Throwable cause) {
    super(cause);
  }

  /**
   * Custom error for when the transaction requested is invalid (e.g. insufficient assets)
   *
   * @param message error message
   */
  public InvalidTransactionException(String message) {
    super(message);
  }
}
