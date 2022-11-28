package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents NFT table.
 */
@Entity
@Table(name = "nft")
public class NFT {

    private String nftId;
    private Float price;

    /**
     * Default constructor for NFT.
     */
    public NFT() {

    }

    /**
     * Constructor for NFT.
     *
     * @param nftId unique Id for nft
     * @param price price of nft
     */
    public NFT(final String nftId, final Float price) {
        this.nftId = nftId;
        this.price = price;
    }

    /**
     * Getter for nftId.
     *
     * @return nftId
     */
    @Id
    public String getNftId() {
        return nftId;
    }

    /**
     * Setter for nftId.
     *
     * @param nftId nftId
     */
    public void setNftId(final String nftId) {
        this.nftId = nftId;
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
     */

    @Override
    public String toString() {
        return "NFT [nftId=" + nftId + ", price=" + price + "]";
    }
}
