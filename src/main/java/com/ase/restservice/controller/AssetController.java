package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.service.AssetService;
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

  @GetMapping("/assets")
  public List<Asset> getAllAssets() {
    return assetService.listAssets("");
  }

  @GetMapping("/assets/{accountId}")
  public List<Asset> getAssetsByAccountId(@PathVariable(value = "accountId") String accountId) {
    return assetService.listAssets(accountId);
  }

  @GetMapping("/assets/{accountId}/{stockId}")
  public Asset getAsset(@PathVariable(value = "accountId") String accountId,
      @PathVariable(value = "stockId") String stockId) throws ResourceNotFoundException {
    return assetService.getAssetById(new AssetId(accountId, stockId));
  }
}