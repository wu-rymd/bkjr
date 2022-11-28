package com.ase.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for when the stock ID queried to some external service (e.g. API) is invalid.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidStockIDException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Constructor with Throwable. Allows exception wrapping via Exception.
   */
  public InvalidStockIDException(Throwable cause) {
    super(cause);
  }

  /**
   * Custom error for when the stock ID queried to some external service (e.g. API) is invalid.
   *
   * @param message error message
   */
  public InvalidStockIDException(String message) {
    super(message);
  }
}
