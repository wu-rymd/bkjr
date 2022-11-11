package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import java.util.List;
import java.util.Optional;

/**
 * Interface for Asset service.
 */
public interface AssetServiceI {
  /**
   * Creates an asset in the database.
   *
   * @param asset Asset
   * @return Created asset
   */
  Asset createAsset(Asset asset);

  /**
   * Updates an asset in the database.
   *
   * @param asset Asset
   * @return Updated asset
   */
  Asset updateAsset(Asset asset);

  /**
   * Deletes an asset in the database.
   *
   * @param assetId AssetID
   */
  void deleteAssetById(AssetId assetId);

  /**
   * Gets an asset by assetId.
   *
   * @param assetId AssetID
   * @return Asset
   * @throws ResourceNotFoundException if asset does not exist in the database
   */
  Asset getAssetById(AssetId assetId) throws ResourceNotFoundException;

  /**
   * Retrieve all assets or those owned by an account.
   *
   * @param accountId AccountID
   * @return List of assets
   */
  List<Asset> listAssets(String accountId);

  /**
   * Calculates the portfolio value of an account.
   *
   * @param accountId AccountID
   * @return Portfolio value
   * @throws ResourceNotFoundException if account does not exist in the database
   */
  Float getAccountPortfolioValue(String accountId) throws ResourceNotFoundException;

  /**
   * Handles buying an asset for an account.
   *
   * @param accountId UUID for which account this transaction belongs to
   * @param stockId   UUID for which stock is being bought
   * @param numShares number of shares request in the buy order
   * @return new Asset
   *
   */
  Asset buyAsset(String accountId, String stockId, Float numShares);

  /**
   * Handles selling an asset for an account.
   *
   * @param accountId AccountID
   * @param stockId StockID
   * @param numShares Number of shares
   * @return Asset remaining in the account
   * @throws Exception if order is invalid
   */
  Optional<Asset> sellAsset(String accountId, String stockId, Float numShares) throws Exception;
}
