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
public final class Transaction {
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

  /**
   * Default constructor for Transaction.
   */
  public Transaction() {  }

  /**
   * Constructor for Transaction.
   *
   * @param accountId Unique ID for account
   * @param stockId Unique ID for stockId
   * @param numShares number of shares being bought/sold
   * @param transactionType Buy or sell
   * @param transactionStatus status of transaction
   */
  public Transaction(
      final String accountId,
      final String stockId,
      final Float numShares,
      final String transactionType,
      final String transactionStatus
  ) {
    this.accountId = accountId;
    this.stockId = stockId;
    this.numShares = numShares;
    this.transactionType = transactionType;
    this.transactionStatus = transactionStatus;
  }

  /**
   * getter for UUID.
   * @return UUID
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Getter for accountId.
   * @return accountId
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * Getter for stockId.
   * @return stockId.
   */
  public String getStockId() {
    return stockId;
  }

  /**
   * Getter for numShares.
   * @return numShares
   */
  public Float getNumShares() {
    return numShares;
  }

  /**
   * Getter for transactionType.
   * @return transactionType
   */
  public String getTransactionType() {
    return transactionType;
  }

  /**
   * Getter for transactionStatus.
   * @return transactionStatus
   */
  public String getTransactionStatus() {
    return transactionStatus;
  }

  /**
   * Setter for TransactionStatus.
   * @param transactionStatus transactionStatus
   */
  public void setTransactionStatus(final String transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  /**
   * Custom equals method.
   * @param o object to compare to this
   * @return true if o and this share the same attributes, false otherwise
   */
  @Override
  public boolean equals(final Object o) {
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

  /**
   * Custom hashcode method.
   * @return hashcode representation of a Transaction
   */
  @Override
  public int hashCode() {
    return Objects.hash(uuid, accountId, stockId, numShares, transactionType, transactionStatus);
  }
}
