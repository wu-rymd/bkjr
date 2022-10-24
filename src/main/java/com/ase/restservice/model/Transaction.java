package com.ase.restservice.model;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represent Transaction table.
 */
@Entity
@Table(name = "transaction")
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;
  @Column(name = "account_id", nullable = false)
  private String accountId;
  @Column(name = "stock_id", nullable = false)
  private String stockId;
  @Column(name = "num_shares", nullable = false)
  private Float numShares;
  @Column(name = "transaction_type", nullable = false)
  private String transactionType;
  @Column(name = "transaction_status", nullable = false)
  private String transactionStatus;

  public Transaction() {}

  /**
   * Constructor for Transaction objects, UUID is generated here.
   */
  public Transaction(
      String accountId,
      String stockId,
      Float numShares,
      String transactionType,
      String transactionStatus
  ) {
    this.accountId = accountId;
    this.stockId = stockId;
    this.numShares = numShares;
    this.transactionType = transactionType;
    this.transactionStatus = transactionStatus;
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getAccountId() {
    return accountId;
  }

  public String getStockId() {
    return stockId;
  }

  public Float getNumShares() {
    return numShares;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getTransactionStatus() {
    return transactionStatus;
  }

  public void setTransactionStatus(String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Transaction)) {
      return false;
    }
    Transaction c = (Transaction) o;
    return Objects.equals(this.getUuid(), c.getUuid())
        && Objects.equals(this.getAccountId(), c.getAccountId())
        && Objects.equals(this.getStockId(), c.getStockId())
        && Objects.equals(this.getNumShares(), c.getNumShares())
        && Objects.equals(this.getTransactionType(), c.getTransactionType())
        && Objects.equals(this.getTransactionStatus(), c.getTransactionStatus());
  }
}
