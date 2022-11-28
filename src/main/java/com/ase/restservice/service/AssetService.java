package com.ase.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;

import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.model.NFT;

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
public class AssetService implements AssetServiceI {

  @Autowired
  private AssetRepository assetRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private StockService stockService;

  @Autowired
  private CryptocurrencyService cryptocurrencyService;

  @Autowired
  private NFTService nftService;

  /**
   * Creates an asset in the database.
   *
   * @param asset Asset
   * @return Created asset
   * @throws ResourceAlreadyExistsException if asset already exists
   */
  public Asset createAsset(Asset asset) throws ResourceAlreadyExistsException {
    if (assetRepository.existsById(asset.getAssetId())) {
      throw new ResourceAlreadyExistsException("Asset already exists");
    }
    return assetRepository.save(asset);
  }

  /**
   * Updates an asset in the database.
   *
   * @param asset Asset
   * @return Updated asset
   * @throws ResourceNotFoundException if asset does not exist
   */
  public Asset updateAsset(Asset asset) throws ResourceNotFoundException {
    if (!assetRepository.existsById(asset.getAssetId())) {
      throw new ResourceNotFoundException("Asset does not exist");
    }
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
      throw new ResourceNotFoundException("Asset " + assetId + " does not exist");
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
            "Asset not found for assetId :: " + assetId));
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
   * Retrieve all assets of given Tradable type or those owned by an account.
   *
   * @param accountId    AccountID
   * @param tradableType Tradable type
   * @return List of assets
   */
  public List<Asset> listAssetsByType(String accountId, String tradableType) {
    if (accountId.isEmpty()) {
      return assetRepository.findAllAssetsByTradableType(tradableType);
    }
    return assetRepository.findAllAssetsByTypeAndId(accountId, tradableType);
  }

  /**
   * Calculates the portfolio value of an account.
   *
   * @param accountId AccountID
   * @return Portfolio value
   * @throws AccountNotFoundException  if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Float getAccountPortfolioValue(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    List<Asset> userAssets = this.listAssets(accountId);
    float total = 0f;
    for (Asset asset : userAssets) {
      if (asset.getTradableType().equals("stock")) {
        Stock stock = stockService.getStockById(asset.getTradableId());
        total += stock.getPrice() * asset.getQuantity();
      } else if (asset.getTradableType().equals("cryptocurrency")) {
        Cryptocurrency crypto = cryptocurrencyService
            .getCryptocurrencyById(asset.getTradableId());
        total += crypto.getPrice() * asset.getQuantity();
      } else if (asset.getTradableType().equals("nft")) {
        NFT nft = nftService.getNFTById(asset.getTradableId());
        total += nft.getPrice() * asset.getQuantity();
      } else {
        throw new ResourceNotFoundException("pnl functions for asset type "
            + asset.getTradableType() + " not implemented");
      }
    }
    return total;
  }

  /**
   * Calculate the total value of an account (portfolio value + cash balance).
   *
   * @param accountId Unique ID for the account
   * @return Total value of an account
   * @throws AccountNotFoundException  if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in database
   */
  public Float getAccountTotalValue(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId));
    Float portfolioValue = getAccountPortfolioValue(accountId);
    Float currentBalance = account.getBalance();

    return currentBalance + portfolioValue;
  }

  /**
   * Calculate and return the percent change between an account's starting balance
   * and current value. If account has net losses, percent change will be
   * negative,
   * if account has net profit, percent change will be positive.
   *
   * @param accountId Unique ID for the account
   * @return Percent change between starting balance and current account value
   * @throws AccountNotFoundException  if account does not exist in the database
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Float getAccountPnl(String accountId)
      throws AccountNotFoundException, ResourceNotFoundException {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException(
            "Account not found for accountId :: " + accountId));
    Float accountValue = getAccountTotalValue(accountId);
    Float startingBalance = account.getStartingBalance();

    return (accountValue - startingBalance) / startingBalance;
  }

  /**
   * Handles buying an asset for an account.
   *
   * @param accountId    UUID for which account this transaction belongs to
   * @param tradableType Tradable type
   * @param tradableId   UUID for which tradable this transaction belongs to
   * @param quantity     Quantity of tradable to buy
   * @return new Asset
   */
  public Asset buyAsset(String accountId, String tradableType, String tradableId, Float quantity)
      throws AccountNotFoundException, ResourceNotFoundException {
    // when buying an asset, first check if it already exists.
    // If exists, then update the quantity
    // If not exists, write a new asset
    AssetId assetId = new AssetId(accountId, tradableType, tradableId);
    Optional<Asset> asset = assetRepository.findById(assetId);
    if (asset.isPresent()) {
      Asset newAsset = asset.get();
      newAsset.setQuantity(newAsset.getQuantity() + quantity);
      assetRepository.save(newAsset);
      return newAsset;
    } else {
      Asset newAsset = new Asset(accountId, tradableType, tradableId, quantity);
      assetRepository.save(newAsset);
      return newAsset;
    }

  }

  /**
   * Handles selling an asset for an account.
   *
   * @param accountId    AccountID
   * @param tradableType Tradable type
   * @param tradableId   TradableID
   * @param quantity     Quantity of tradable to sell
   * @return Asset remaining in the account
   * @throws ResourceNotFoundException   if the user does not have an asset of the
   *                                     given type
   * @throws InvalidTransactionException if the user does not have enough of the
   *                                     asset to sell
   */
  public Optional<Asset> sellAsset(String accountId,
      String tradableType,
      String tradableId,
      Float quantity)
      throws ResourceNotFoundException, InvalidTransactionException {
    Optional<Asset> asset = assetRepository.findById(
        new AssetId(accountId, tradableType, tradableId));
    if (asset.isPresent()) {
      // Check whether user is selling all of their asset
      Asset userAsset = asset.get();
      if (Objects.equals(userAsset.getQuantity(), quantity)) {
        // Delete the asset
        this.deleteAssetById(new AssetId(accountId, tradableType, tradableId));
        return Optional.empty();
      } else if (userAsset.getQuantity() < quantity) {
        throw new InvalidTransactionException("Insufficient amount of asset to sell");
      } else {
        userAsset.setQuantity(userAsset.getQuantity() - quantity);
        this.updateAsset(userAsset);
        return Optional.of(userAsset);
      }
    } else {
      throw new ResourceNotFoundException("Asset of the tradable type"
          + tradableType + "with the id " + tradableId
          + " does not exist for the account: "
          + accountId);
    }
  }
}
