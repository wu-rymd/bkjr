package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.service.AccountServiceI;
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
   * Updates the balance of an account.
   *
   * @param accountId AccountID
   * @param amount Value that will be summed with balance
   * @return Updated balance
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  public Float updateAccountBalance(String accountId, String amount)
      throws ResourceNotFoundException {
    Account account = this.getAccountById(accountId);
    account.setBalance(account.getBalance() + Float.parseFloat(amount));
    final Account updatedAccount = this.updateAccount(account);
    return updatedAccount.getBalance();
  }

}
