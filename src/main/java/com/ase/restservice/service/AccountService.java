package com.ase.restservice.service;

import com.ase.restservice.exception.AccountAlreadyExistsException;
import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Account operations.
 */
@Service
public class AccountService implements AccountServiceI {

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Creates an account in the database.
   *
   * @param account Account
   * @return Created account
   * @throws AccountAlreadyExistsException if account ID already exists in database
   */
  public Account createAccount(Account account) throws AccountAlreadyExistsException {
    try {
      String accountId = account.getAccountId();
      Account dbAccount = this.getAccountById(accountId);

      // reach here means the account already exists in database
      throw new AccountAlreadyExistsException(
        "Account already exists with ID :: " + accountId
      );
    } catch (AccountNotFoundException e) {
      // reach here means account does not already exist in database
      return accountRepository.save(account);
    }
  }

  /**
   * Updates an account in the database.
   *
   * @param account Account
   * @return Updated account
   */
  public Account updateAccount(Account account) throws AccountNotFoundException {
    try {
      String accountId = account.getAccountId();
      Account dbAccount = this.getAccountById(accountId);
      return accountRepository.save(account);
    } catch (AccountNotFoundException e) {
      throw new AccountNotFoundException(e);
    }
  }

  /**
   * Deletes an account in the database.
   *
   * @param accountId AccountID
   */
  public void deleteAccountById(String accountId) throws AccountNotFoundException {
    try {
      Account dbAccount = this.getAccountById(accountId);
      accountRepository.deleteById(accountId);
    } catch (AccountNotFoundException e) {
      throw new AccountNotFoundException(e);
    }
  }

  /**
   * Gets an account by accountId.
   *
   * @param accountId AccountID
   * @return Account
   * @throws AccountNotFoundException if account does not exist in the database
   */
  public Account getAccountById(String accountId) throws AccountNotFoundException {
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
  }

  /**
   * Method to increase or decrease a user's account balance.
   *
   * @param accountId Primary key of account
   * @param amount dollar amount to change account balance by. If negative, will decrease the
   *               account balance.
   * @return Returns the account with the updated balance
   * @throws AccountNotFoundException if account does not exist in the database
   */
  public Account updateAccountBalance(String accountId, Float amount)
      throws AccountNotFoundException {
    Account account = this.getAccountById(accountId);
    account.setBalance(account.getBalance() + amount);
    final Account updatedAccount = this.updateAccount(account);
    return updatedAccount;
  }

}
