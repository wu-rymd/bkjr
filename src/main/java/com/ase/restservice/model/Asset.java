package com.ase.restservice.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="asset")
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
    public String getStockId() { return assetId.getStockId();}

    @Column(name = "numShares", nullable = false)
    public Float getNumShares() {
        return numShares;
    }
    public void setNumShares(Float numShares) {
        this.numShares = numShares;
    }
}