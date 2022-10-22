package com.ase.restservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ase.restservice.model.Asset;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String>{
     @Query(value = "SELECT * FROM Asset WHERE asset.stock_id = ?1", nativeQuery = true)
     List<Asset> findAllAssetsByStockId(String stockId);

     @Query(value = "SELECT * FROM Asset WHERE asset.account_id = ?1", nativeQuery = true)
     List<Asset> findAllAssetsByAccountId(String accountId);
}