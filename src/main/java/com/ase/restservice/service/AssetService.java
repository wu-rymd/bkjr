package com.ase.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Asset operations.
 */
@Service
public class AssetService implements com.ase.restservice.service.AssetServiceI {

  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private StockService stockService;

  /**
   * Creates an asset in the database.
   *
   * @param asset Asset
   * @return Created asset
   */
  public Asset createAsset(Asset asset) {
    // TODO: Throw exception if asset already exists
    return assetRepository.save(asset);
  }

  /**
   * Updates an asset in the database.
   *
   * @param asset Asset
   * @return Updated asset
   */
  public Asset updateAsset(Asset asset) {
    // TODO: Throw exception if asset does not exist
    return assetRepository.save(asset);
  }

  /**
   * Deletes an asset in the database.
   *
   * @param assetId AssetID
   * @throws ResourceNotFoundException if asset is not found in database
   */
  public void deleteAssetById(AssetId assetId) throws ResourceNotFoundException {
    Optional<Asset> asset = assetRepository.findById(assetId);
    if (asset.isPresent()) {
      assetRepository.deleteById(assetId);
    } else {
      throw new ResourceNotFoundException(
          "Asset " + assetId + " does not exist");
    }
  }

  /**
   * Gets an asset by assetId.
   *
   * @param assetId AssetID
   * @return Asset
   * @throws ResourceNotFoundException if asset does not exist in the database
   */
  public Asset getAssetById(AssetId assetId) throws ResourceNotFoundException {
    return assetRepository.findById(assetId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Asset not found for assetId :: " + assetId
        ));
  }

  /**
   * Retrieve all assets or those owned by an account.
   *
   * @param accountId AccountID
   * @return List of assets
   */
  public List<Asset> listAssets(String accountId) {
    if (accountId.isEmpty()) {
      return assetRepository.findAll();
    }
    return assetRepository.findAllAssetsByAccountId(accountId);
  }

  /**
   * Calculates the portfolio value of an account.
   *
   * @param accountId AccountID
   * @return Portfolio value
   * @throws AccountNotFoundException if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Float getAccountPortfolioValue(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    List<Asset> userAssets = this.listAssets(accountId);
    float total = 0f;
    String stockId;
    Stock stock;
    Float price;
    for (Asset asset : userAssets) {
      // Total value of a given asset is the current share price * the # of shares the account owns
      stockId = asset.getStockId();
      stock = stockService.getStockById(stockId);
      price = stock.getPrice();
      total += price * asset.getNumShares();
    }
    return total;
  }

  /**
   * Calculate the total value of an account (portfolio value + cash balance).
   *
   * @param accountId Unique ID for the account
   * @return Total value of an account
   * @throws AccountNotFoundException if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in database
   */
  public Float getAccountTotalValue(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
    Float portfolioValue = getAccountPortfolioValue(accountId);
    Float currentBalance = account.getBalance();

    return currentBalance + portfolioValue;
  }

  /**
   * Calculate and return the percent change between an account's starting balance
   * and current value. If account has net losses, percent change will be negative,
   * if account has net profit, percent change will be positive.
   *
   * @param accountId Unique ID for the account
   * @return Percent change between starting balance and current account value
   * @throws AccountNotFoundException if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Float getAccountPnl(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId
        ));
    Float accountValue = getAccountTotalValue(accountId);
    Float startingBalance = account.getStartingBalance();

    return (accountValue - startingBalance) / startingBalance;
  }

  /**
   * Handles buying an asset for an account.
   *
   * @param accountId UUID for which account this transaction belongs to
   * @param stockId   UUID for which stock is being bought
   * @param numShares number of shares request in the buy order
   * @return new Asset
   */
  public Asset buyAsset(String accountId, String stockId, Float numShares) {
    // when buying an asset, first check if it already exists.
    // If exists, then update the stock amount
    // If not exists, write a new asset
    Optional<Asset> asset = assetRepository.findById(new AssetId(accountId, stockId));
    if (asset.isPresent()) {
      // update the current asset
      Asset currentAsset = asset.get();
      currentAsset.setNumShares(currentAsset.getNumShares() + numShares);
      assetRepository.save(currentAsset);
      return currentAsset;
    } else {
      // new asset
      Asset newAsset = new Asset(accountId, stockId, numShares);
      assetRepository.save(newAsset);
      return newAsset;
    }
  }

  /**
   * Handles selling an asset for an account.
   *
   * @param accountId AccountID
   * @param stockId StockID
   * @param numShares Number of shares
   * @return Asset remaining in the account
   * @throws ResourceNotFoundException if the user does not have an asset/stock
   * @throws InvalidTransactionException if the number of shares is insufficient
   */
  public Optional<Asset> sellAsset(String accountId, String stockId, Float numShares)
      throws ResourceNotFoundException, InvalidTransactionException {
    Optional<Asset> asset = assetRepository.findById(new AssetId(accountId, stockId));
    if (asset.isPresent()) {
      // Check whether user is selling all of their asset
      Asset userAsset = asset.get();
      if (Objects.equals(userAsset.getNumShares(), numShares)) {
        // Delete the asset
        this.deleteAssetById(new AssetId(accountId, stockId));
        return Optional.empty();
      }
      if (userAsset.getNumShares() < numShares) {
        throw new InvalidTransactionException("Insufficient shares");
      } else {
        userAsset.setNumShares(userAsset.getNumShares() - numShares);
        this.updateAsset(userAsset);
        return Optional.of(userAsset);
      }
    } else {
      throw new ResourceNotFoundException(
          "Asset " + stockId + " does not exist for user " + accountId);
    }
  }
}
