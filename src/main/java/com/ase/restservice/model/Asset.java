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
public class Asset {

  @EmbeddedId
  private AssetId assetId;
  private Float numShares;

  public Asset() {
  }

  public Asset(String accountId, String stockId, Float numShares) {
    this.assetId = new AssetId(accountId, stockId);
    this.numShares = numShares;
  }

  public String getAccountId() {
    return assetId.getAccountId();
  }

  public String getStockId() {
    return assetId.getStockId();
  }

  @Column(name = "num_shares", nullable = false)
  public Float getNumShares() {
    return numShares;
  }

  public void setNumShares(Float numShares) {
    this.numShares = numShares;
  }

  @Override
  public boolean equals(Object o) {
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
}