package com.ase.restservice.model;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represent Order table.
 */
@Entity
@Table(name = "stock_order")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;
  @Column(name = "account_id", nullable = false)
  private String accountId;
  @Column(name = "stock_id", nullable = false)
  private String stockId;
  @Column(name = "num_shares", nullable = false)
  private Float numShares;
  @Column(name = "order_type", nullable = false)
  private String orderType;
  @Column(name = "order_status", nullable = false)
  private String orderStatus;

  public Order() {}

  /**
   * Constructor for Order objects, UUID is generated here.
   */
  public Order(
      String accountId,
      String stockId,
      Float numShares,
      String orderType,
      String orderStatus
  ) {
    this.accountId = accountId;
    this.stockId = stockId;
    this.numShares = numShares;
    this.orderType = orderType;
    this.orderStatus = orderStatus;
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

  public String getOrderType() {
    return orderType;
  }

  public String getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }
}
