package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.repository.StockRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private StockRepository stockRepository;    // call the service instead when it's created?

  public List<Asset> getAssetsByAccountId(String accountId) {
    return assetRepository.findAllAssetsByAccountId(accountId);
  }

  public Float getAccountPortfolioValue(String accountId) throws ResourceNotFoundException {
    List<Asset> userAssets = this.getAssetsByAccountId(accountId);
    float total = 0f;
    for (Asset asset : userAssets) {
      // Total value of a given asset is the current share price * the # of shares the account owns
      total += stockRepository.findById(asset.getStockId()).orElseThrow(() ->
              new ResourceNotFoundException("Stock not found for stock id ::" + asset.getStockId()))
          .getPrice() * asset.getNumShares();
    }
    return total;
  }

  /**
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

  public Optional<Asset> sellAsset(String accountId, String stockId, Float numShares)
      throws Exception {
    Optional<Asset> asset = assetRepository.findById(new AssetId(accountId, stockId));
    if (asset.isPresent()) {
      // Check whether user is selling all of their asset
      Asset userAsset = asset.get();
      if (Objects.equals(userAsset.getNumShares(), numShares)) {
        // Delete the asset
        assetRepository.deleteById(new AssetId(accountId, stockId));
        return Optional.empty();
      }
      if (userAsset.getNumShares() < numShares) {
        throw new Exception("INVALID SELL ORDER");
      } else {
        userAsset.setNumShares(userAsset.getNumShares() - numShares);
        assetRepository.save(userAsset);
        return Optional.of(userAsset);
      }
    } else {
      throw new ResourceNotFoundException(
          "Asset " + stockId + " does not exist for user " + accountId);
    }
  }
}
