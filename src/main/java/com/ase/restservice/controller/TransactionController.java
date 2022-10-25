package com.ase.restservice.controller;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /transactions endpoints.
 */
@RestController
public class TransactionController {
  @Autowired
  TransactionService transactionService;

  /**
   * Create a new transaction.
   *
   * @param transaction Transaction
   * @return Asset
   * @throws Exception if transaction is invalid
   */
  @Operation(summary = "Create transaction given Transaction object")
  @PostMapping("/transactions")
  public Optional<Asset> postAsset(@RequestBody Transaction transaction) throws Exception {
    return transactionService.createTransaction(transaction);
  }

}
