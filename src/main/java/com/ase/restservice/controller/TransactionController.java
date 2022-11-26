package com.ase.restservice.controller;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidOrderTypeException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
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
  private TransactionService transactionService;

  /**
   * Create a new transaction.
   *
   * @param transaction Transaction
   * @return Asset
   * @throws AccountNotFoundException if account is not found in database
   * @throws ResourceNotFoundException if user does not have the asset
   * @throws InvalidOrderTypeException when transaction type is not buy or sell
   * @throws InvalidTransactionException if user does not have sufficient assets
   */
  @Operation(summary = "Create transaction given Transaction object")
  @PostMapping("/transactions")
  public Optional<Asset> postAsset(@RequestBody final Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {
    return transactionService.createTransaction(transaction);
  }

}
