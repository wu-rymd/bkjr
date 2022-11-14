package com.ase.restservice.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents Asset table.
 */
@Entity
@Table(name = "asset")
public final class Asset {

  @EmbeddedId
  private AssetId assetId;
  private Float numShares;

  /**
   * Default constructor.
   */
  public Asset() {
  }

  /**
   * Creates an asset object.
   *
   * @param accountId unique identifier for account
   * @param stockId   unique identifier for stock
   * @param numShares number of shares of stock owned by account
   */
  public Asset(final String accountId, final String stockId, final Float numShares) {
    this.assetId = new AssetId(accountId, stockId);
    this.numShares = numShares;
  }

  /**
   * Getter for accountId.
   *
   * @return accountId
   */
  public String getAccountId() {
    return assetId.getAccountId();
  }

  /**
   * Getter for stockId.
   *
   * @return stockId
   */
  public String getStockId() {
    return assetId.getStockId();
  }

  /**
   * Getter for numShares.
   *
   * @return Float number of shares
   */
  @Column(name = "num_shares", nullable = false)
  public Float getNumShares() {
    return numShares;
  }

  /**
   * Setter for numShares.
   *
   * @param numShares Float number of shares
   */
  public void setNumShares(final Float numShares) {
    this.numShares = numShares;
  }

  /**
   * Custom equals to compare objects with Asset object.
   *
   * @param o object to compare to this
   * @return true if attributes of this and o are equal, else false
   */
  @Override
  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Asset)) {
      return false;
    }
    Asset c = (Asset) o;
    return Objects.equals(this.getAccountId(), c.getAccountId())
        && Objects.equals(this.getStockId(), c.getStockId())
        && Objects.equals(this.getNumShares(), c.getNumShares());

  }

  @Override
  public int hashCode() {
    return Objects.hash(assetId, numShares);
  }
}
