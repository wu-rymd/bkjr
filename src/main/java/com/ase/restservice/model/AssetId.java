package com.ase.restservice.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents (account_id, stock_id) composite key.
 */
public final class AssetId implements Serializable {

  private String accountId;
  private String stockId;

  /**
   * Default constructor for AssetId.
   */
  public AssetId() {
  }

  /**
   * Constructor for AssetId.
   *
   * @param accountId Unique ID for account
   * @param stockId Unique Id for stock
   */
  public AssetId(final String accountId, final String stockId) {
    this.accountId = accountId;
    this.stockId = stockId;
  }

  /**
   * Getter for AccountId.
   * @return accountId
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * Getter for StockId.
   * @return stockId
   */
  public String getStockId() {
    return stockId;
  }

  /**
   * Custom equals function.
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
        && Objects.equals(this.getStockId(), c.getStockId());

  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, stockId);
  }
}
