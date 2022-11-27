package com.ase.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
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
     * @throws ResourceAlreadyExistsException if asset already exists
     */
    Asset createAsset(Asset asset);

    /**
     * Updates an asset in the database.
     *
     * @param asset Asset
     * @return Updated asset
     * @throws ResourceNotFoundException if asset does not exist
     */
    Asset updateAsset(Asset asset);

    /**
     * Deletes an asset in the database.
     *
     * @param assetId AssetID
     * @throws ResourceNotFoundException if asset is not found in database
     */
    void deleteAssetById(AssetId assetId) throws ResourceNotFoundException;

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
     * Retrieve all assets of given Tradable type or those owned by an account.
     *
     * @param accountId    AccountID
     * @param tradableType Tradable type
     * @return List of assets
     */
    List<Asset> listAssetsByType(String accountId, String tradableType);

    /**
     * Calculates the portfolio value of an account.
     *
     * @param accountId AccountID
     * @return Portfolio value
     * @throws AccountNotFoundException  if account does not exist in the database
     * @throws ResourceNotFoundException if stock does not exist in the database
     */
    Float getAccountPortfolioValue(String accountId)
            throws AccountNotFoundException, ResourceNotFoundException;

    /**
     * Calculate the total value of an account (portfolio value + cash balance).
     *
     * @param accountId Unique ID for the account
     * @return Total value of an account
     * @throws AccountNotFoundException  if account does not exist in the database
     * @throws ResourceNotFoundException if tradable does not exist in database
     */
    Float getAccountTotalValue(String accountId)
            throws AccountNotFoundException, ResourceNotFoundException;

    /**
     * Calculate and return the percent change between an account's starting balance
     * and current value. If account has net losses, percent change will be
     * negative,
     * if account has net profit, percent change will be positive.
     *
     * @param accountId Unique ID for the account
     * @return Percent change between starting balance and current account value
     * @throws AccountNotFoundException if account does not exist in the database
     */
    Float getAccountPnl(String accountId)
            throws AccountNotFoundException, ResourceNotFoundException;

    /**
     * Handles buying an asset for an account.
     *
     * @param accountId    UUID for which account this transaction belongs to
     * @param tradableType Tradable type
     * @param tradableId   UUID for the tradable being bought
     * @param quantity     Quantity of tradable being bought
     * @return new Asset
     *
     */
    Asset buyAsset(String accountId, String tradableType, String tradableId, Integer quantity)
            throws ResourceNotFoundException, AccountNotFoundException;

    /**
     * Handles selling an asset for an account.
     *
     * @param accountId    AccountID
     * @param tradableType Tradable type
     * @param tradableId   Tradable id
     * @param quantity     Quantity of tradable being sold
     * @return Asset remaining in the account
     * @throws Exception if order is invalid
     */
    Optional<Asset> sellAsset(String accountId, String tradableType, String tradableId, Integer quantity)
            throws Exception;
}
