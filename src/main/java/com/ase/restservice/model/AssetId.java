package com.ase.restservice.model;

import java.io.Serializable;
import java.util.Objects;

public class AssetId implements Serializable {

  private String accountId;
  private String stockId;

  public AssetId() {
  }

  ;

  public AssetId(String accountId, String stockId) {
    this.accountId = accountId;
    this.stockId = stockId;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getStockId() {
    return stockId;
  }

  @Override
  public boolean equals(Object o) {
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
}

