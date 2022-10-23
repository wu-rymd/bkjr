package com.ase.restservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ase.restservice.model.Account;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetService assetService;
    @PostMapping("/accounts")
    public Account createAccount(@Valid @RequestBody Account account) {
        // TODO: Throw exception if account already exists
        return accountRepository.save(account);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public Account getAccountBalance(@PathVariable(value = "accountId") String accountId) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + accountId));
        return account;
    }

    @PutMapping("/accounts/{accountId}/balance")
    public Account updateAccountBalance(@PathVariable(value = "accountId") String accountId, @RequestParam(value = "amount", defaultValue = "0") String amount) throws ResourceNotFoundException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for this id :: " + accountId));
        account.setBalance(account.getBalance() + Float.parseFloat(amount));
        final Account updatedAccount = accountRepository.save(account);
        return updatedAccount;
    }

    @GetMapping("/accounts/{accountId}/portfolio_value")   // TODO: Come up with a better name for this endpoint
    public Float getAccountAssetsValue(@PathVariable(value="accountId") String accountId) {
        return assetService.getAccountPortfolioValue(accountId);
    }
    @GetMapping("/accounts/{accountId}/pnl")
    public Float getAccountPnl(@PathVariable(value="accountId") String accountId) throws ResourceNotFoundException {
        return assetService.getAccountPnl(accountId);

    }
}