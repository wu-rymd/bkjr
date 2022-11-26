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
@Service
public class CryptocurrencyService implements CryptocurrencyServiceI {

    @Autowired
    private CryptocurrencyRepository cryptocurrencyRepository;

    /**
     * Creates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Created cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency already exists in the
     *                                   database
     */
    public Cryptocurrency createCryptocurrency(Cryptocurrency cryptocurrency)
            throws ResourceNotFoundException {
        if (cryptocurrencyRepository.existsById(cryptocurrency.getCryptocurrencyId())) {
            throw new ResourceNotFoundException("Cryptocurrency already exists");
        }
        return cryptocurrencyRepository.save(cryptocurrency);
    }

    /**
     * Updates a cryptocurrency in the database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Updated cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    public Cryptocurrency updateCryptocurrency(Cryptocurrency cryptocurrency)
            throws ResourceNotFoundException {
        if (!cryptocurrencyRepository.existsById(cryptocurrency.getCryptocurrencyId())) {
            throw new ResourceNotFoundException("Cryptocurrency does not exist");
        }
        return cryptocurrencyRepository.save(cryptocurrency);
    }

    /**
     * Deletes a cryptocurrency in the database.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    public void deleteCryptocurrencyById(String cryptocurrencyId)
            throws ResourceNotFoundException {
        if (!cryptocurrencyRepository.existsById(cryptocurrencyId)) {
            throw new ResourceNotFoundException("Cryptocurrency does not exist");
        }
        cryptocurrencyRepository.deleteById(cryptocurrencyId);
    }

    /**
     * Gets a cryptocurrency by cryptocurrencyId.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     *                                   database
     */
    public Cryptocurrency getCryptocurrencyById(String cryptocurrencyId)
            throws ResourceNotFoundException {
        return cryptocurrencyRepository.findById(cryptocurrencyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Cryptocurrency not found for this id :: " + cryptocurrencyId));
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
        Cryptocurrency cryptocurrency = getCryptocurrencyById(cryptocurrencyId);
        cryptocurrency.setPrice(price);
        return updateCryptocurrency(cryptocurrency);
    }

    /**
     * Gets all cryptocurrencies in the database.
     *
     * @return List of cryptocurrencies
     */
    public List<Cryptocurrency> getAllCryptocurrencies() {
        return cryptocurrencyRepository.findAll();
    }

    /**
     * Gets the price of a cryptocurrency.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency price
     * @throws ResourceNotFoundException if cryptocurrency does not exist in the
     */
    public Float getCryptocurrencyPrice(String cryptocurrencyId) throws ResourceNotFoundException {
        return getCryptocurrencyById(cryptocurrencyId).getPrice();
    }

}
