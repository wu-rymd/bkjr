package com.ase.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidOrderTypeException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.repository.TransactionRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Transaction operations.
 */
@Service
public final class TransactionService implements TransactionServiceI {
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
   * @throws AccountNotFoundException if account is not found in database
   * @throws ResourceNotFoundException if user does not have the asset
   * @throws InvalidOrderTypeException when transaction type is not buy or sell
   * @throws InvalidTransactionException if user does not have sufficient assets
   */
  public Optional<Asset> createTransaction(Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {
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
   * @throws AccountNotFoundException if account is not found in database
   * @throws ResourceNotFoundException if user does not have the asset
   * @throws InvalidOrderTypeException when transaction type is not buy or sell
   * @throws InvalidTransactionException if user does not have sufficient assets
   */
  public Optional<Asset> executeTransaction(Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {
    Stock stock = stockService.getStockById(transaction.getStockId());
    String transactionType = transaction.getTransactionType();
    if (Objects.equals(transactionType, "BUY")) {
      return Optional.of(buyTransaction(transaction, stock));
    } else if (Objects.equals(transactionType, "Sell")) {
      return sellTransaction(transaction, stock);
    } else {
      throw new InvalidOrderTypeException("Invalid order type :: " + transactionType);
    }
  }

  /**
   * Executes buy transactions by doing the following: Updating/creating account asset,
   * updating account balance, updating transaction status.
   *
   * @param transaction Transaction object to be executed, with transactionType="BUY"
   * @param stock Stock to be bought
   * @return account's updated asset after the buyTransaction has been executed
   * @throws AccountNotFoundException if account does not exist in the database
   */
  public Asset buyTransaction(Transaction transaction, Stock stock)
      throws AccountNotFoundException {
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
   * @throws AccountNotFoundException if account does not exist in the database
   * @throws InvalidTransactionException if transaction type is not buy or sell
   * @throws ResourceNotFoundException if user does not have sufficient assets
   */
  public Optional<Asset> sellTransaction(Transaction transaction, Stock stock)
      throws AccountNotFoundException, InvalidTransactionException, ResourceNotFoundException {
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

  /**
   * List all transactions for an account given accountId.
   * @param accountId Unique identifier for account
   * @return List of transactions belonging to account with accountId
   *
   * @throws AccountNotFoundException if account does not exist
   */
  public List<Transaction> listAccountTransactions(String accountId)
      throws AccountNotFoundException {
    return transactionRepository.findByAccountId(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
  }

  /**
   * List all transactions.
   * @return list of all transactions
   */
  public List<Transaction> listAllTransactions() {
    return transactionRepository.findAll();
  }
}
