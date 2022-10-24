package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents Stock table.
 */
@Entity
@Table(name = "stock")
public class Stock {

  private String stockId;
  private Float price;

  public Stock() {

  }

  public Stock(String stockId, Float price) {
    this.stockId = stockId;
    this.price = price;
  }

  @Id
  public String getStockId() {
    return stockId;
  }

  public void setStockId(String stockId) {
    this.stockId = stockId;
  }

  @Column(name = "price", nullable = false)
  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Stock [stockId=" + stockId + ", price=" + price + "]";
  }
}