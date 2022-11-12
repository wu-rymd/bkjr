package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.repository.TransactionRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Transaction operations.
 */
@Service
public class TransactionService implements TransactionServiceI {
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired private AssetService assetService;
  @Autowired private StockService stockService;
  @Autowired
  private AccountService accountService;

  /**
   * Write a new transaction to the database.
   * @param transaction new Transaction
   * @return returns the asset that was created/affected by this transaction
   * @throws Exception if user does not exist
   */
  public Optional<Asset> createTransaction(Transaction transaction) throws Exception {
    transactionRepository.save(transaction);
    return executeTransaction(transaction);
  }

  /**
   * Update a transaction status in the database.
   * @param transaction transaction to update
   * @param status new status
   */
  public void updateTransactionStatus(Transaction transaction, String status) {
    transaction.setTransactionStatus(status);
    transactionRepository.save(transaction);
  }

  /**
   * Executes transactions to buy/sell assets. Directs to helper methods based on transaction type.
   *
   * @param transaction Transaction object placed
   * @return return the updated asset unless the asset was deleted (in the case the user sold
   *        all the shares of the asset), then return null.
   *
   * @throws Exception when transaction is invalid, or required resources do not exist in database
   */
  public Optional<Asset> executeTransaction(Transaction transaction) throws Exception {
    Stock stock = stockService.getStockById(transaction.getStockId());
    if (Objects.equals(transaction.getTransactionType(), "BUY")) {
      return Optional.of(buyTransaction(transaction, stock));
    } else if (Objects.equals(transaction.getTransactionType(), "Sell")) {
      return sellTransaction(transaction, stock);
    } else {
      throw new Exception("INVALID ORDER TYPE");
    }
  }

  /**
   * Executes buy transactions by doing the following: Updating/creating account asset,
   * updating account balance, updating transaction status.
   *
   * @param transaction Transaction object to be executed, with transactionType="BUY"
   * @param stock Stock to be bought
   * @return account's updated asset after the buyTransaction has been executed
   * @throws ResourceNotFoundException if account does not exist
   */
  public Asset buyTransaction(Transaction transaction, Stock stock)
      throws ResourceNotFoundException {
    // UPDATE/CREATE ASSET
    Asset newAsset = assetService.buyAsset(
        transaction.getAccountId(),
        transaction.getStockId(),
        transaction.getNumShares()
    );
    float totalCost = stock.getPrice() * transaction.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // send the (-) amount of total_cost so that the account service DECREASES the account's balance
    accountService.updateAccountBalance(transaction.getAccountId(), -totalCost);
    updateTransactionStatus(transaction, "COMPLETED");
    return newAsset;
  }

  /**
   * Executes sell transaction by doing the following: Updating/deleting account asset,
   * updating account balance, updating transaction status.
   *
   * @param transaction Transaction object to be executed, with transactionType="SELL"
   * @param stock Stock to be sold
   * @return account's updated asset after sellTransaction has been excecuted, return null in
   *        the case that all the asset has been sold (asset has been deleted)
   * @throws Exception if invalid sell, or required resources do not exist
   */
  public Optional<Asset> sellTransaction(Transaction transaction, Stock stock) throws Exception {
    // UPDATE/DELETE ASSET
    Optional<Asset> newAsset = assetService.sellAsset(
        transaction.getAccountId(),
        stock.getStockId(),
        transaction.getNumShares());
    float totalCost = stock.getPrice() * transaction.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // SEND THE (+) amount of total_cost so tht the account service INCREASES account's balance
    accountService.updateAccountBalance(transaction.getAccountId(), totalCost);
    updateTransactionStatus(transaction, "COMPLETED");
    return newAsset;
  }
}
