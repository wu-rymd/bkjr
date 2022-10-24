package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;

/**
 * Interface for Account service.
 */
public interface AccountServiceI {
  Account createAccount(Account account);

  Account updateAccount(Account account);

  void deleteAccountById(String accountId);

  Account getAccountById(String accountId) throws ResourceNotFoundException;

  Float updateAccountBalance(String accountId, String amount) throws ResourceNotFoundException;
}