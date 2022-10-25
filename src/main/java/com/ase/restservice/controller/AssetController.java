package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /assets endpoints.
 */
@RestController
public class AssetController {
  @Autowired
  private AssetService assetService;

  @Operation(summary = "Get list of all assets")
  @GetMapping("/assets")
  public List<Asset> getAllAssets() {
    return assetService.listAssets("");
  }

  @Operation(summary = "Get list of assets of account given accountId")
  @GetMapping("/assets/{accountId}")
  public List<Asset> getAssetsByAccountId(@PathVariable(value = "accountId") String accountId) {
    return assetService.listAssets(accountId);
  }

  @Operation(summary = "Get asset given accountId and stockId")
  @GetMapping("/assets/{accountId}/{stockId}")
  public Asset getAsset(@PathVariable(value = "accountId") String accountId,
      @PathVariable(value = "stockId") String stockId) throws ResourceNotFoundException {
    return assetService.getAssetById(new AssetId(accountId, stockId));
  }
}