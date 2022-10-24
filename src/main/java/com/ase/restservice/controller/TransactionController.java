package com.ase.restservice.controller;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.service.TransactionService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /transaction endpoint.
 */
@RestController
public class TransactionController {
  @Autowired
  TransactionService transactionService;

  @PostMapping("/transaction")
  public Optional<Asset> postAsset(@RequestBody Transaction transaction) throws Exception {
    return transactionService.createTransaction(transaction);
  }

}
