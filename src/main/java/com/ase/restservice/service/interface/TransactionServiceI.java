package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import java.util.Optional;

/**
 * Interface for Transaction service.
 */
public interface TransactionServiceI {
  Optional<Asset> createTransaction(Transaction transaction) throws Exception;

  void updateTransactionStatus(Transaction transaction, String status);

  Optional<Asset> executeTransaction(Transaction transaction) throws Exception;

  Asset buyTransaction(Transaction transaction, Stock stock) throws ResourceNotFoundException;

  Optional<Asset> sellTransaction(Transaction transaction, Stock stock) throws Exception;
}
