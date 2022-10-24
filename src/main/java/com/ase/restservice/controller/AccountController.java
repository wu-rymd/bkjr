package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
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
  private AccountService accountService;
  @Autowired
  private AssetService assetService;

  /**
   * Create new account.
   *
   * @param account Account
   * @return Updated account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @Operation(summary = "create an account with given id and balance")
  @PostMapping("/accounts")
  public Account createAccount(@Valid @RequestBody Account account) {
    return accountService.createAccount(account);
  }

  /**
   * Retrieve account balance.
   *
   * @param accountId AccountID
   * @return Updated account
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @Operation(summary = "get balance of account with given accountId")
  @GetMapping("/accounts/{accountId}/balance")
  public Float getAccountBalance(@PathVariable(value = "accountId") String accountId)
      throws ResourceNotFoundException {
    Account account = accountService.getAccountById(accountId);
    return account.getBalance();
  }

  /**
   * Update account balance.
   *
   * @param accountId AccountID
   * @param amount Value that will be summed with balance
   * @return Updated balance
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  @Operation(summary = "update balance of account with given accountId")
  @PutMapping("/accounts/{accountId}/balance")
  public Float updateAccountBalance(@PathVariable(value = "accountId") String accountId,
      @RequestParam(value = "amount", defaultValue = "0") String amount)
      throws ResourceNotFoundException {
    return accountService.updateAccountBalance(accountId, amount);
  }

  @GetMapping("/accounts/{accountId}/portfolio_value")
  public Float getAccountPortfolioValue(@PathVariable(value = "accountId") String accountId)
      throws ResourceNotFoundException {
    return assetService.getAccountPortfolioValue(accountId);
  }
}