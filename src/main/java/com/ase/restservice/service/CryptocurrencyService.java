package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.repository.CryptocurrencyRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Cryptocurrency operations.
 */
public class CryptocurrencyService implements CryptocurrencyServiceI {

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    /**
     * Creates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Created cryptocurrency
     */
    public Cryptocurrency createCryptocurrency(Cryptocurrency cryptocurrency) {

    }

    /**
     * Updates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Updated cryptocurrency
     */
    public Cryptocurrency updateCryptocurrency(Cryptocurrency cryptocurrency) {

    }

    /**
     * Deletes a cryptocurrency in the database.
     *
     * @param cryptocurrencyId CryptocurrencyID
     */
    public void deleteCryptocurrencyById(String cryptocurrencyId) {

    }

    /**
     * Gets a cryptocurrency by cryptocurrencyId.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    public Cryptocurrency getCryptocurrencyById(String cryptocurrencyId) throws ResourceNotFoundException {

    }

    /**
     * Updates the price of a cryptocurrency.
     * 
     * @param cryptocurrencyId CryptocurrencyID
     * @param price            Cryptocurrency price
     * @return Updated cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     */
    public Cryptocurrency updateCryptocurrencyPrice(String cryptocurrencyId, Float price)
            throws ResourceNotFoundException {

    }

    /**
     * Gets all cryptocurrencies in the database.
     *
     * @return List of cryptocurrencies
     */
    public List<Cryptocurrency> getAllCryptocurrencies() {

    }

    /**
     * Gets the price of a cryptocurrency.
     * 
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency price
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     */
    public Float getCryptocurrencyPrice(String cryptocurrencyId) throws ResourceNotFoundException {

    }

}
