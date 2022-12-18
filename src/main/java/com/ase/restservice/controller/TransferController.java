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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class TransferController {
  @Autowired
  private TransactionService transactionService;

  @Operation(summary = "Transfer assets from sender to reciever")
  @PostMapping("/transfer/{recipientId}")
  public Optional<Asset> executeTransfer(@PathVariable final String recipientId, @RequestBody
  final Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {
    return transactionService.executeTransfer(transaction, recipientId);
  }
}
