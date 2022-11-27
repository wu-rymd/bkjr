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
   * Native query to get all assets of a given tradable_type and tradable_id.
   *
   * @param tradableType Tradable type
   * @param tradableId   Tradable id
   * @return List of assets of the given (tradable_type, tradable_id)
   */
  @Query(value = "SELECT * FROM Asset WHERE tradable_type = ?1 AND tradable_id = ?2", nativeQuery = true)
  List<Asset> findAllAssetsByTypeAndId(String tradableType, String tradableId);

  /**
   * Native query to get all assets that belong to a given account id.
   *
   * @param accountId unique identifier for account
   * @return list of assets that belong to the given account id
   */
  @Query(value = "SELECT * FROM Asset WHERE asset.account_id = ?1", nativeQuery = true)
  List<Asset> findAllAssetsByAccountId(String accountId);
}
