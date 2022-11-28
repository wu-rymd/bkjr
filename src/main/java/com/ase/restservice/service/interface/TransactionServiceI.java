package com.ase.restservice.service;

import com.ase.restservice.exception.*;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Transaction service.
 */
public interface TransactionServiceI {
      /**
       * Write a new transaction to the database.
       * 
       * @param transaction new Transaction
       * @return returns the asset that was created/affected by this transaction
       * @throws Exception if user does not exist
       */
      Optional<Asset> createTransaction(Transaction transaction)
                  throws AccountNotFoundException, ResourceNotFoundException,
                  InvalidOrderTypeException, InvalidTransactionException;

      /**
       * Update a transaction status in the database.
       * 
       * @param transaction transaction to update
       * @param status      new status
       */
      void updateTransactionStatus(Transaction transaction, String status);

      /**
       * Executes transactions to buy/sell assets. Directs to helper methods based on
       * transaction type.
       *
       * @param transaction Transaction object placed
       * @return return the updated asset unless the asset was deleted (in the case
       *         the user sold
       *         all the shares of the asset), then return null.
       * @throws AccountNotFoundException  if account is not found in database
       * @throws ResourceNotFoundException if user does not have the asset
       * @throws InvalidOrderTypeException when transaction type is not buy or sell
       */
      Optional<Asset> executeTransaction(Transaction transaction) throws Exception;

      /**
       * Executes buy transactions by doing the following: Updating/creating account
       * asset,
       * updating account balance, updating transaction status.
       *
       * @param transaction Transaction object to be executed, with
       *                    transactionType="BUY"
       * @return account's updated asset after the buyTransaction has been executed
       * @throws ResourceNotFoundException if account does not exist
       */
      Asset buyTransaction(Transaction transaction) throws ResourceNotFoundException;

      /**
       * Executes sell transaction by doing the following: Updating/deleting account
       * asset,
       * updating account balance, updating transaction status.
       *
       * @param transaction Transaction object to be executed, with
       *                    transactionType="SELL"
       * @param stock       Stock to be sold
       * @return account's updated asset after sellTransaction has been excecuted,
       *         return null in
       *         the case that all the asset has been sold (asset has been deleted)
       * @throws Exception if invalid sell, or required resources do not exist
       */
      Optional<Asset> sellTransaction(Transaction transaction) throws Exception;

      /**
       * List all transactions for an account given accountId.
       * 
       * @param accountId Unique identifier for account
       * @return List of transactions belonging to account with accountId
       */
      List<Transaction> listAccountTransactions(String accountId)
                  throws AccountNotFoundException;

      /**
       * List all transactions.
       * 
       * @return list of all transactions
       */
      List<Transaction> listAllTransactions();

      Optional<Asset> sellTransaction(Transaction transaction, Stock stock)
                  throws AccountNotFoundException, InvalidTransactionException, ResourceNotFoundException;
}
