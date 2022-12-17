package com.ase.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidOrderTypeException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;

import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.model.NFT;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.ase.restservice.ApplicationSecurity.getUsernameOfClientLogged;

/**
 * Service for Transaction operations.
 */
@Service
public final class TransactionService implements TransactionServiceI {
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AssetService assetService;
  @Autowired
  private StockService stockService;
  @Autowired
  private CryptocurrencyService cryptocurrencyService;
  @Autowired
  private NFTService nftService;
  @Autowired
  private AccountService accountService;
  @Value("${com.ase.restservice.ApplicationSecurity.production}")
  private Boolean production;

  /**
   * Write a new transaction to the database.
   *
   * @param transaction new Transaction
   * @return returns the asset that was created/affected by this transaction
   * @throws AccountNotFoundException    if account is not found in database
   * @throws ResourceNotFoundException   if user does not have the asset
   * @throws InvalidOrderTypeException   when transaction type is not buy or sell
   * @throws InvalidTransactionException if user does not have sufficient assets
   */
  public Optional<Asset> createTransaction(Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {

//      transactionRepository.save(transaction);
//      return executeTransaction(transaction);
    //need to check if the client
    if (production) {
      String accountId = transaction.getAccountId();
      Account account = accountRepository.findAccountsByAccountId(accountId).orElseThrow(() ->
          new UsernameNotFoundException("Account Not Found with username: " + accountId));

      String clientId = getUsernameOfClientLogged();
      if (account.getClientId().equals(clientId)) {
        transactionRepository.save(transaction);
        return executeTransaction(transaction);
      }
      throw new AccountNotFoundException("Bad client");
    } else {
      transactionRepository.save(transaction);
      return executeTransaction(transaction);
    }
  }

  /**
   * Update a transaction status in the database.
   *
   * @param transaction transaction to update
   * @param status      new status
   */
  public void updateTransactionStatus(Transaction transaction, String status) {
    transaction.setTransactionStatus(status);
    transactionRepository.save(transaction);
  }

  /**
   * Executes transactions to buy/sell assets. Directs to helper methods based on
   * transaction type.
   *
   * @param transaction Transaction object placed
   * @return return the updated asset unless the asset was deleted (in the case
   * the user sold
   * all the shares of the asset), then return null.
   * @throws AccountNotFoundException    if account is not found in database
   * @throws ResourceNotFoundException   if user does not have the asset
   * @throws InvalidOrderTypeException   when transaction type is not buy or sell
   * @throws InvalidTransactionException if user does not have sufficient assets
   */
  public Optional<Asset> executeTransaction(Transaction transaction)
      throws AccountNotFoundException, ResourceNotFoundException,
      InvalidOrderTypeException, InvalidTransactionException {
    if (transaction.getTransactionType().equals("BUY")) {
      return Optional.of(buyTransaction(transaction));
    } else if (transaction.getTransactionType().equals("SELL")) {
      return sellTransaction(transaction);
    } else {
      throw new InvalidOrderTypeException("Invalid transaction type");
    }
  }

  /**
   * Executes buy transactions by doing the following: Updating/creating account
   * asset,
   * updating account balance, updating transaction status.
   *
   * @param transaction Transaction object to be executed, with
   *                    transactionType="BUY"
   * @return account's updated asset after the buyTransaction has been executed
   * @throws AccountNotFoundException if account does not exist in the database
   */
  public Asset buyTransaction(Transaction transaction)
      throws AccountNotFoundException, InvalidTransactionException, ResourceNotFoundException,
      InvalidOrderTypeException {
    // UPDATE/CREATE ASSET
    Asset newAsset = assetService.buyAsset(transaction.getAccountId(),
        transaction.getTradableType(),
        transaction.getTradableId(),
        transaction.getQuantity());

    // UPDATE ACCOUNT BALANCE
    // send the (-) amount of total_cost so that the account service DECREASES
    // account's balance
    Float totalCost;

    if (transaction.getTradableType().equals("stock")) {
      Stock stock = stockService.getStockById(transaction.getTradableId());
      totalCost = stock.getPrice() * transaction.getQuantity();
    } else if (transaction.getTradableType().equals("cryptocurrency")) {
      Cryptocurrency cryptocurrency = cryptocurrencyService
          .getCryptocurrencyById(transaction.getTradableId());
      totalCost = cryptocurrency.getPrice() * transaction.getQuantity();
    } else if (transaction.getTradableType().equals("nft")) {
      NFT nft = nftService.getNFTById(transaction.getTradableId());
      totalCost = nft.getPrice() * transaction.getQuantity();
    } else {
      throw new InvalidOrderTypeException("Invalid tradable type");
    }

    accountService.updateAccountBalance(transaction.getAccountId(), -totalCost);
    updateTransactionStatus(transaction, "COMPLETED");
    return newAsset;
  }

  /**
   * Executes sell transaction by doing the following: Updating/deleting account
   * asset,
   * updating account balance, updating transaction status.
   *
   * @param transaction Transaction object to be executed, with
   *                    transactionType="SELL"
   * @return account's updated asset after sellTransaction has been excecuted,
   * return null in
   * the case that all the asset has been sold (asset has been deleted)
   * @throws AccountNotFoundException    if account does not exist in the database
   * @throws InvalidTransactionException if transaction type is not buy or sell
   * @throws ResourceNotFoundException   if user does not have sufficient assets
   */
  public Optional<Asset> sellTransaction(Transaction transaction)
      throws AccountNotFoundException, InvalidTransactionException, ResourceNotFoundException,
      InvalidOrderTypeException {
    // UPDATE/DELETE ASSET
    Optional<Asset> newAsset = assetService.sellAsset(transaction.getAccountId(),
        transaction.getTradableType(),
        transaction.getTradableId(),
        transaction.getQuantity());

    // UPDATE ACCOUNT BALANCE
    // SEND THE (+) amount of total_cost so tht the account service INCREASES
    // account's balance
    Float totalCost;

    if (transaction.getTradableType().equals("stock")) {
      Stock stock = stockService.getStockById(transaction.getTradableId());
      totalCost = stock.getPrice() * transaction.getQuantity();
    } else if (transaction.getTradableType().equals("cryptocurrency")) {
      Cryptocurrency cryptocurrency = cryptocurrencyService
          .getCryptocurrencyById(transaction.getTradableId());
      totalCost = cryptocurrency.getPrice() * transaction.getQuantity();
    } else if (transaction.getTradableType().equals("nft")) {
      NFT nft = nftService.getNFTById(transaction.getTradableId());
      totalCost = nft.getPrice() * transaction.getQuantity();
    } else {
      throw new InvalidOrderTypeException("Invalid tradable type");
    }

    accountService.updateAccountBalance(transaction.getAccountId(), totalCost);
    updateTransactionStatus(transaction, "COMPLETED");
    return newAsset;
  }

  /**
   * List all transactions for an account given accountId.
   *
   * @param accountId Unique identifier for account
   * @return List of transactions belonging to account with accountId
   * @throws AccountNotFoundException if account does not exist
   */
  public List<Transaction> listAccountTransactions(String accountId)
      throws AccountNotFoundException {
    return transactionRepository.findByAccountId(accountId)
        .orElseThrow(
            () -> new AccountNotFoundException("Account not found for accountId :: "
                + accountId));
  }

  /**
   * List all transactions.
   *
   * @return list of all transactions
   */
  public List<Transaction> listAllTransactions() {
    String clientId = getUsernameOfClientLogged();
    return transactionRepository.listAllTransactionsOfClient(clientId);
  }
}
