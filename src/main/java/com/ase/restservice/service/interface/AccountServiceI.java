package com.ase.restservice.service;

import com.ase.restservice.exception.AccountAlreadyExistsException;
import com.ase.restservice.exception.AccountNotFoundException;
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
   * @throws AccountAlreadyExistsException if account ID already exists in database
   */
  Account createAccount(Account account) throws AccountAlreadyExistsException;

  /**
   * Updates an account in the database.
   *
   * @param account Account
   * @return Updated account
   */
  Account updateAccount(Account account) throws AccountNotFoundException;

  /**
   * Deletes an account in the database.
   *
   * @param accountId AccountID
   */
  void deleteAccountById(String accountId) throws AccountNotFoundException;

  /**
   * Gets an account by accountId.
   *
   * @param accountId AccountID
   * @return Account
   * @throws AccountNotFoundException if account does not exist in the database
   */
  Account getAccountById(String accountId) throws AccountNotFoundException;

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
   * @param amount    dollar amount to change account balance by. If negative, will decrease the
   *                  account balance.
   * @return Returns the account with the updated balance
   * @throws AccountNotFoundException if account does not exist in the database
   */
  Account updateAccountBalance(String accountId, Float amount) throws AccountNotFoundException;
}
