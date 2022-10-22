package com.ase.restservice.model;

import java.io.Serializable;

public class AssetId implements Serializable {
    private String accountId;
    private String stockId;
    public AssetId(){};
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
}
