package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.service.StockService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Asset operations.
 */
@Service
public class AssetService {

  @Autowired
  private AssetRepository assetRepository;

  @Autowired
  private StockService stockService;

  /**
   * Saves an asset to the database.
   *
   * @param asset Asset
   * @return Updated asset
   */
  public Asset save(Asset asset) {
    // TODO: Throw exception if asset already exists
    return assetRepository.save(asset);
  }

  /**
   * Deletes an asset to the database.
   *
   * @param assetId AssetID
   */
  public void deleteById(AssetId assetId) {
    assetRepository.deleteById(assetId);
  }

  /**
   * Gets an asset by assetId.
   *
   * @param assetId AssetID
   * @return Asset
   * @throws ResourceNotFoundException if asset does not exist in the database
   */
  public Asset findById(AssetId assetId) throws ResourceNotFoundException {
    return assetRepository.findById(assetId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Asset not found for assetId :: " + assetId
        ));
  }

  /**
   * Retrieve all assets.
   *
   * @return List of assets
   */
  public List<Asset> findAll() {
    return assetRepository.findAll();
  }

  /**
   * Retrieve all assets of an account.
   *
   * @param accountId AccountID
   * @return List of assets
   */
  public List<Asset> findAllAssetsByAccountId(String accountId) {
    return assetRepository.findAllAssetsByAccountId(accountId);
  }

  /**
   * Calculates the portfolio value of an account.
   *
   * @param accountId AccountID
   * @return Portfolio value
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  public Float getAccountPortfolioValue(String accountId) throws ResourceNotFoundException {
    List<Asset> userAssets = this.findAllAssetsByAccountId(accountId);
    float total = 0f;
    String stockId;
    Stock stock;
    Float price;
    for (Asset asset : userAssets) {
      // Total value of a given asset is the current share price * the # of shares the account owns
      stockId = asset.getStockId();
      System.out.println("STOCK ID " + stockId);
      stock = stockService.findById(stockId);
      System.out.println(stock);
      price = stock.getPrice();
      total += price * asset.getNumShares();
    }
    return total;
  }

  /**
   * Handles buying an asset for an account.
   *
   * @param accountId UUID for which account this transaction belongs to
   * @param stockId   UUID for which stock is being bought
   * @param numShares number of shares request in the buy order
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
   * @throws Exception if order is invalid
   */
  public Optional<Asset> sellAsset(String accountId, String stockId, Float numShares)
      throws Exception {
    Optional<Asset> asset = assetRepository.findById(new AssetId(accountId, stockId));
    if (asset.isPresent()) {
      // Check whether user is selling all of their asset
      Asset userAsset = asset.get();
      if (Objects.equals(userAsset.getNumShares(), numShares)) {
        // Delete the asset
        this.deleteById(new AssetId(accountId, stockId));
        return Optional.empty();
      }
      if (userAsset.getNumShares() < numShares) {
        throw new Exception("INVALID SELL ORDER");
      } else {
        userAsset.setNumShares(userAsset.getNumShares() - numShares);
        this.save(userAsset);
        return Optional.of(userAsset);
      }
    } else {
      throw new ResourceNotFoundException(
          "Asset " + stockId + " does not exist for user " + accountId);
    }
  }
}
