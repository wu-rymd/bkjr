package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Account operations.
 */
@Service
public class AccountService implements com.ase.restservice.service.AccountServiceI {

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Creates an account in the database.
   *
   * @param account Account
   * @return Created account
   */
  public Account createAccount(Account account) {
    // TODO: Throw exception if account already exists
    return accountRepository.save(account);
  }

  /**
   * Updates an account in the database.
   *
   * @param account Account
   * @return Updated account
   */
  public Account updateAccount(Account account) {
    // TODO: Throw exception if account does not exist
    return accountRepository.save(account);
  }

  /**
   * Deletes an account in the database.
   *
   * @param accountId AccountID
   */
  public void deleteAccountById(String accountId) {
    // TODO: Throw exception if account does not exist
    accountRepository.deleteById(accountId);
  }

  /**
   * Gets an account by accountId.
   *
   * @param accountId AccountID
   * @return Account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  public Account getAccountById(String accountId) throws ResourceNotFoundException {
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException(
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
   * @throws ResourceNotFoundException when account does not exist
   */
  public Account updateAccountBalance(String accountId, Float amount)
      throws ResourceNotFoundException {
    Account account = this.getAccountById(accountId);
    account.setBalance(account.getBalance() + amount);
    final Account updatedAccount = this.updateAccount(account);
    return updatedAccount;
  }

}
