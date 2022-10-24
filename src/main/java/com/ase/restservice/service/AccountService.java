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
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Saves an account to the database.
   *
   * @param account Account
   * @return Updated account
   */
  public Account save(Account account) {
    // TODO: Throw exception if account already exists
    return accountRepository.save(account);
  }

  /**
   * Gets an account by accountId.
   *
   * @param accountId AccountID
   * @return Account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  public Account findById(String accountId) throws ResourceNotFoundException {
    return accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
  }

  public Account updateAccountBalance(String accountId, Float amount) throws ResourceNotFoundException{
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
    account.setBalance(account.getBalance()+amount);
    accountRepository.save(account);
    return account;
  }
}
