package com.ase.restservice.controller;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.repository.AssetRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AssetController {
  @Autowired
  private AssetRepository assetRepository;

  @GetMapping("/assets")
  public List<Asset> getAllAssets() {
    return assetRepository.findAll();
  }

  @GetMapping("/assets/{accountId}")
  public List<Asset> getAssetsByUserId(@PathVariable(value = "accountId") String accountId) {
    return assetRepository.findAllAssetsByAccountId(accountId);
  }

  @GetMapping("/assets/{accountId}/{stockId}")
  public Optional<Asset> getAsset(@PathVariable(value = "accountId") String accountId,
                                  @PathVariable(value = "stockId") String stockId) {
    return assetRepository.findById(new AssetId(accountId, stockId));
  }
}