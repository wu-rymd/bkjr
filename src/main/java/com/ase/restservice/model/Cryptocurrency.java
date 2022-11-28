package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents Cryptocurrency table.
 */
@Entity
@Table(name = "cryptocurrency")
public class Cryptocurrency {

    private String cryptocurrencyId;
    private Float price;

    /**
     * Default constructor for Cryptocurrency.
     */
    public Cryptocurrency() {

    }

    /**
     * Constructor for Cryptocurrency.
     *
     * @param cryptocurrencyId unique Id for cryptocurrency
     * @param price            price of cryptocurrency
     */
    public Cryptocurrency(final String cryptocurrencyId, final Float price) {
        this.cryptocurrencyId = cryptocurrencyId;
        this.price = price;
    }

    /**
     * Getter for cryptocurrencyId.
     *
     * @return cryptocurrencyId
     */
    @Id
    public String getCryptocurrencyId() {
        return cryptocurrencyId;
    }

    /**
     * Setter for cryptocurrencyId.
     *
     * @param cryptocurrencyId cryptocurrencyId
     */
    public void setCryptocurrencyId(final String cryptocurrencyId) {
        this.cryptocurrencyId = cryptocurrencyId;
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
     * @param price price
     */
    public void setPrice(final Float price) {
        this.price = price;
    }

    /**
     * Override toString method.
     *
     * @return String representation of Cryptocurrency
     */

    @Override
    public String toString() {
        return "Cryptocurrency [cryptocurrencyId=" + cryptocurrencyId + ", price=" + price + "]";
    }
}
