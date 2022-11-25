package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import java.util.List;

/**
 * Interface for Account service.
 */
public interface AccountServiceI {
  /**
   * Creates an account in the database.
   *
   * @param account Account
   * @return Created account
   */
  Account createAccount(Account account);

  /**
   * Updates an account in the database.
   *
   * @param account Account
   * @return Updated account
   */
  Account updateAccount(Account account);

  /**
   * Deletes an account in the database.
   *
   * @param accountId AccountID
   */
  void deleteAccountById(String accountId);

  /**
   * Gets an account by accountId.
   *
   * @param accountId AccountID
   * @return Account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  Account getAccountById(String accountId) throws ResourceNotFoundException;

  /**
   * List all accounts.
   *
   * @return list of accounts
   */
  List<Account> listAllAccounts();

  /**
   * Method to increase or decrease a user's account balance.
   *
   * @param accountId Primary key of account
   * @param amount dollar amount to change account balance by. If negative, will decrease the
   *               account balance.
   * @return Returns the account with the updated balance
   * @throws ResourceNotFoundException when account does not exist
   */
  Account updateAccountBalance(String accountId, Float amount) throws ResourceNotFoundException;
}
