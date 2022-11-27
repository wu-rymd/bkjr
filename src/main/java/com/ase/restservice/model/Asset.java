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
  private Float quantity;

  /**
   * Default constructor.
   */
  public Asset() {
  }

  /**
   * Creates an asset object.
   *
   * @param accountId    unique identifier for account
   * @param tradableType type of tradable
   * @param tradableId   unique identifier for tradable
   * @param quantity     number of tradables
   */
  public Asset(final String accountId, final String tradableType, final String tradableId,
      final Float quantity) {
    this.assetId = new AssetId(accountId, tradableType, tradableId);
    this.quantity = quantity;
  }

  /**
   * Getter for assetId.
   *
   * @return assetId
   */
  public AssetId getAssetId() {
    return assetId;
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
   * Getter for tradableType.
   *
   * @return tradableType
   */
  public String getTradableType() {
    return assetId.getTradableType();
  }

  /**
   * Getter for tradableId.
   *
   * @return tradableId
   */
  public String getTradableId() {
    return assetId.getTradableId();
  }

  /**
   * Getter for quantity.
   *
   * @return Float number of tradables
   */
  @Column(name = "quantity", nullable = false)
  public Float getQuantity() {
    return quantity;
  }

  /**
   * Setter for quantity.
   *
   * @param quantity number of tradables
   */
  public void setQuantity(final Float quantity) {
    this.quantity = quantity;
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
        && Objects.equals(this.getTradableType(), c.getTradableType())
        && Objects.equals(this.getTradableId(), c.getTradableId())
        && Objects.equals(this.getQuantity(), c.getQuantity());
  }

  @Override
  public int hashCode() {
    return Objects.hash(assetId, quantity);
  }
}
