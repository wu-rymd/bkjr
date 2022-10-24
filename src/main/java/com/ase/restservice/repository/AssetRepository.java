package com.ase.restservice.repository;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, AssetId> {

  @Query(value = "SELECT * FROM Asset WHERE asset.stock_id = ?1", nativeQuery = true)
  List<Asset> findAllAssetsByStockId(String stockId);

  @Query(value = "SELECT * FROM Asset WHERE asset.account_id = ?1", nativeQuery = true)
  List<Asset> findAllAssetsByAccountId(String accountId);
}