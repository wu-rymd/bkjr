package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Cryptocurrency;

import java.util.List;

/**
 * Interface for Cryptocurrency service.
 */
public interface CryptocurrencyServiceI {
    /**
     * Creates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Created cryptocurrency
     */
    Cryptocurrency createCryptocurrency(Cryptocurrency cryptocurrency);

    /**
     * Retrieve all cryptocurrencies.
     *
     * @return List of cryptocurrencies
     */
    List<Cryptocurrency> getAllCryptocurrencies();

    /**
     * Updates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Updated cryptocurrency
     */
    Cryptocurrency updateCryptocurrency(Cryptocurrency cryptocurrency);

    /**
     * Deletes a cryptocurrency in the database.
     *
     * @param cryptocurrencyId CryptocurrencyID
     */
    void deleteCryptocurrencyById(String cryptocurrencyId);

    /**
     * Gets a cryptocurrency by cryptocurrencyId.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    Cryptocurrency getCryptocurrencyById(String cryptocurrencyId) throws ResourceNotFoundException;

    /**
     * Updates the price of a cryptocurrency.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @param price            Cryptocurrency price
     * @return Updated cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    Cryptocurrency updateCryptocurrencyPrice(String cryptocurrencyId, Float price)
            throws ResourceNotFoundException;

    /**
     * Gets the price of a cryptocurrency by cryptocurrencyId.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency price
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    Float getCryptocurrencyPrice(String cryptocurrencyId) throws ResourceNotFoundException;
}
