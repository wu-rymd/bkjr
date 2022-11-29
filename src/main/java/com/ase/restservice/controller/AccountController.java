package com.ase.restservice.controller;

import com.ase.restservice.exception.AccountAlreadyExistsException;
import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


import static com.ase.restservice.ApplicationSecurity.getUsernameOfClientLogged;

/**
 * Controller for /accounts endpoints.
 */
@RestController
public final class AccountController {

  @Autowired
  private AccountService accountService;
  @Autowired
  private AssetService assetService;
  @Autowired
  private TransactionService transactionService;

  /**
   * Create new account.
   *
   * @param account Account
   * @return Updated account
   * @throws AccountAlreadyExistsException if account already exists in the
   *                                       database
   */
  @Operation(summary = "Create account given Account object")
  @PostMapping("/accounts")
  public Account createAccount(@Valid @RequestBody final Account account)
      throws AccountAlreadyExistsException {
    return accountService.createAccount(account);
  }

  /**
   * Retrieve an account.
   *
   * @param accountId AccountID
   * @return account Account
   * @throws AccountNotFoundException if account does not exist in the database
   */
  @Operation(summary = "Get account given accountId")
  @GetMapping("/accounts/{accountId}")
  public Account getAccount(@PathVariable(value = "accountId") final String accountId)
      throws AccountNotFoundException {
    return accountService.getAccountById(accountId);
  }

  /**
   * Retrieve account balance.
   *
   * @param accountId AccountID
   * @return Account balance
   * @throws AccountNotFoundException if account does not exist in the database
   */
  @Operation(summary = "Get balance of account given accountId")
  @GetMapping("/accounts/{accountId}/balance")
  public Float getAccountBalance(@PathVariable(value = "accountId") final String accountId)
      throws AccountNotFoundException {
    return accountService.getAccountById(accountId).getBalance();
  }

  /**
   * Update account balance.
   *
   * @param accountId AccountID
   * @param amount    Value that will be summed with balance
   * @return Updated balance
   * @throws AccountNotFoundException if account does not exist in the database
   */
  @Operation(summary = "Update balance of account given accountId")
  @PutMapping("/accounts/{accountId}/balance")
  public Account updateAccountBalance(@PathVariable(value = "accountId") final String accountId,
                                      @RequestParam(value = "amount", defaultValue = "0")
                                      final Float amount)
      throws AccountNotFoundException {
    return accountService.updateAccountBalance(accountId, amount);
  }

  /**
   * Get portfolio value of an account.
   *
   * @param accountId AccountID
   * @return Portfolio value
   * @throws AccountNotFoundException  if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "Get portfolio value of account given accountId")
  @GetMapping("/accounts/{accountId}/portfolio_value")
  public Float getAccountPortfolioValue(@PathVariable(value = "accountId") final String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    return assetService.getAccountPortfolioValue(accountId);
  }

  /**
   * List all transactions for an account given accountId.
   *
   * @param accountId Unique identifier for account
   * @return List of all transactions for account with accountId
   */
  @Operation(summary = "List all transactions (buy/sell orders) for an account given accountId")
  @GetMapping("/accounts/{accountId}/transactions")
  public List<Transaction> listAccountTransactions(
      @PathVariable(value = "accountId") String accountId) throws AccountNotFoundException {
    return transactionService.listAccountTransactions(accountId);
  }

  /**
   * List all accounts.
   *
   * @return List of all accounts that are owned by the client logged in
   */
  @Operation(summary = "List all accounts owned by the client")
  @GetMapping("/accounts")
  public List<Account> listAllAccountsByCLient() {
    String clientId = getUsernameOfClientLogged();
    return accountService.listAllAccountsOfClient(clientId);
  }

  /**
   * * Get percent different between starting balance and current account value.
   * This represents the
   * account's net profit/loss.
   *
   * @param accountId Unique Identifier for an account
   * @return Percent difference between account starting balance and current
   * account value
   * @throws AccountNotFoundException  if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "Get a profits/losses for an account given accountId")
  @GetMapping("/accounts/{accountId}/pnl")
  public Float getAccountPnl(@PathVariable(value = "accountId") String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    return assetService.getAccountPnl(accountId);
  }
  @Operation(summary = "Account is deleted by the client")
  @DeleteMapping("accounts/{accountId}")
  public Void deleteAccount(@PathVariable(value = "accountId") String accountId)
    throws AccountNotFoundException, ResourceNotFoundException {
      accountService.deleteAccountById(accountId);
      return null;
  }
}
