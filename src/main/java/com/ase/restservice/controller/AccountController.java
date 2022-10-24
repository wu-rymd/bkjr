package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.service.AssetService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /accounts endpoints.
 */
@RestController
public class AccountController {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private AssetService assetService;

  /**
   * Create new account.
   *
   * @param account Account
   * @return Updated account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @PostMapping("/accounts")
  public Account createAccount(@Valid @RequestBody Account account) {
    // TODO: Throw exception if account already exists
    return accountRepository.save(account);
  }

  /**
   * Retrieve account balance.
   *
   * @param accountId AccountID
   * @return Updated account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @GetMapping("/accounts/{accountId}/balance")
  public Account getAccountBalance(@PathVariable(value = "accountId")
      String accountId) throws ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Account not found for this id :: " + accountId
        ));
    return account;
  }

  /**
   * Update account balance.
   *
   * @param accountId AccountID
   * @param amount    Value that will be summed with balance
   * @return Updated account.
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @PutMapping("/accounts/{accountId}/balance")
  public Account updateAccountBalance(@PathVariable(value = "accountId")
      String accountId, @RequestParam(value = "amount", defaultValue = "0") String amount)
      throws ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Account not found for this id :: " + accountId
        ));
    account.setBalance(account.getBalance() + Float.parseFloat(amount));
    final Account updatedAccount = accountRepository.save(account);
    return updatedAccount;
  }

  @GetMapping("/accounts/{accountId}/portfolio_value")
  public Float getAccountAssetsValue(@PathVariable(value = "accountId") String accountId)
      throws ResourceNotFoundException {
    return assetService.getAccountPortfolioValue(accountId);
  }
}