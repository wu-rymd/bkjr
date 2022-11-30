package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /assets endpoints.
 */
@RestController
public final class AssetController {
  @Autowired
  private AssetService assetService;

  /**
   * Endpoint to get all assets.
   *
   * @return every asset in the database
   */
  @Operation(summary = "Get list of all assets")
  @GetMapping("/assets")
  public List<Asset> getAllAssets() {
    return assetService.listAssets("");
  }

  /**
   * Endpoint to get all assets of a given type.
   *
   * @param tradableType Tradable type
   * @return every asset of the given type in the database
   */
  @Operation(summary = "Get list of all assets of a given type")
  @GetMapping("/assets/{accountId}/{tradableType}")
  public List<Asset> getAllAssetsByType(@PathVariable final String accountId,
      @PathVariable final String tradableType) {
    return assetService.listAssetsByType(accountId, tradableType);
  }

  /**
   * Get all assets belonging to an account given accountId.
   *
   * @param accountId unique identifier of account
   * @return list of assets owned by account
   */
  @Operation(summary = "Get list of assets of account given accountId")
  @GetMapping("/assets/{accountId}")
  public List<Asset> getAssetsByAccountId(
      @PathVariable(value = "accountId") final String accountId) {
    return assetService.listAssets(accountId);
  }

  /**
   * Get an asset for a given account and a given tradable type and id.
   *
   * @param accountId    unique identifier for account
   * @param tradableType type of tradable
   * @param tradableId   unique identifier for tradable
   * @return the asset with given stockId for given accountId
   * @throws ResourceNotFoundException if asset does not exist
   */
  @Operation(summary = "Get asset given accountId and stockId")
  @GetMapping("/assets/{accountId}/{tradableType}/{tradableId}")
  public Asset getAsset(@PathVariable(value = "accountId") final String accountId,
      @PathVariable(value = "tradableType") final String tradableType,
      @PathVariable(value = "tradableId") final String tradableId)
      throws ResourceNotFoundException {
    return assetService.getAssetById(new AssetId(accountId, tradableType, tradableId));
  }
}
