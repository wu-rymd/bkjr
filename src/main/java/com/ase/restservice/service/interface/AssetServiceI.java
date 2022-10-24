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
  Asset createAsset(Asset asset);

  Asset updateAsset(Asset asset);

  void deleteAssetById(AssetId assetId);

  Asset getAssetById(AssetId assetId) throws ResourceNotFoundException;

  List<Asset> listAssets(String accountId);

  Float getAccountPortfolioValue(String accountId) throws ResourceNotFoundException;

  Asset buyAsset(String accountId, String stockId, Float numShares);

  Optional<Asset> sellAsset(String accountId, String stockId, Float numShares) throws Exception;
}
