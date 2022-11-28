package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    /**
     * Default constructor for Stock.
     */
    public Stock() {

    }

    /**
     * Constructor for Stock.
     *
     * @param stockId unique Id for stock
     * @param price   price of stock
     */
    public Stock(final String stockId, final Float price) {
        this.stockId = stockId;
        this.price = price;
    }

    /**
     * Getter for stockId.
     *
     * @return stockID
     */
    @Id
    public String getStockId() {
        return stockId;
    }

    /**
     * Setter for stockID.
     *
     * @param stockId stockId
     */
    public void setStockId(final String stockId) {
        this.stockId = stockId;
    }

    /**
     * Getter for price.
     *
     * @return price
     */
    @Column(name = "price", nullable = false)
    public Float getPrice() {
        return price;
    }

    /**
     * Setter for price.
     *
     * @param price stock price
     */
    public void setPrice(final Float price) {
        this.price = price;
    }

    /**
     * Custom toString method.
     *
     * @return string representation of stock
     */
    @Override
    public String toString() {
        return "Stock [stockId=" + stockId + ", price=" + price + "]";
    }
}
