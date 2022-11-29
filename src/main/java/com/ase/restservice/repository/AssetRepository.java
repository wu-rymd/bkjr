package com.ase.restservice.repository;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Interface for querying the Asset table.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, AssetId> {

  /**
   * Native query to get all assets of a given stock id.
   *
   * @param stockId unique identifier for a stock
   * @return List of assets of the given stock
   */
  @Query(value = "SELECT * FROM Asset WHERE asset.stock_id = ?1", nativeQuery = true)
  List<Asset> findAllAssetsByStockId(String stockId);

  /**
   * Native query to get all assets that belong to a given account id.
   *
   * @param accountId unique identifier for account
   * @return list of assets that belong to the given account id
   */
  @Query(value = "SELECT * FROM Asset WHERE asset.account_id = ?1", nativeQuery = true)
  List<Asset> findAllAssetsByAccountId(String accountId);

  @Query(value = "SELECT * FROM Asset,Account WHERE account.account_id = asset.account_id "
          +
          "and account.client_id = ?1", nativeQuery = true)
  List<Asset> findAllforClient(String clientId);
}
