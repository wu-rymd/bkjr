package com.ase.restservice.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents (account_id, tradable_type, tradable_id) composite key.
 * Tradable_type can be "stock", "crpto", or "nft"
 */
public final class AssetId implements Serializable {

    private String accountId;
    private String tradableType;
    private String tradableId;

    /**
     * Default constructor for AssetId.
     */
    public AssetId() {
    }

    /**
     * Constructor for AssetId.
     *
     * @param accountId    unique identifier for account
     * @param tradableType type of tradable
     * @param tradableId   unique identifier for tradable
     */
    public AssetId(final String accountId, final String tradableType, final String tradableId) {
        this.accountId = accountId;
        this.tradableType = tradableType;
        this.tradableId = tradableId;
    }

    /**
     * Getter for AccountId.
     *
     * @return accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Getter for TradableType.
     *
     * @return tradableType
     */
    public String getTradableType() {
        return tradableType;
    }

    /**
     * Getter for TradableId.
     *
     * @return tradableId
     */
    public String getTradableId() {
        return tradableId;
    }

    /**
     * Custom equals function.
     *
     * @param o object to compare equality
     * @return true if attributes of this and o are all equal, false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AssetId)) {
            return false;
        }
        AssetId c = (AssetId) o;
        return Objects.equals(this.getAccountId(), c.getAccountId())
                && Objects.equals(this.getTradableType(), c.getTradableType())
                && Objects.equals(this.getTradableId(), c.getTradableId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, tradableType, tradableId);
    }
}
