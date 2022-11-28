package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.service.CryptocurrencyService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /cryptocurrencies endpoints.
 */
@RestController
public final class CryptocurrencyController {

    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    /**
     * Create a new cryptocurrency. Checks if cryptocurrency ID is valid before
     * storing in database.
     *
     * @param cryptocurrency Cryptocurrency
     * @return Updated cryptocurrency
     */
    @Operation(summary = "Create cryptocurrency given Cryptocurrency object")
    @PostMapping("/cryptocurrencies")
    public Cryptocurrency createCryptocurrency(
            @Valid @RequestBody final Cryptocurrency cryptocurrency)
            throws ResourceAlreadyExistsException {
        return cryptocurrencyService.createCryptocurrency(cryptocurrency);
    }

    /**
     * Endpoint to get all cryptocurrencies available in the database.
     *
     * @return a list of all available cryptocurrencies in database
     */
    @Operation(summary = "List all cryptocurrencies")
    @GetMapping("/cryptocurrencies")
    public List<Cryptocurrency> listCryptocurrencies() {
        return cryptocurrencyService.getAllCryptocurrencies();
    }

    /**
     * Retrieve a cryptocurrency's price.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @return Cryptocurrency price
     * @throws ResourceNotFoundException if cryptocurrency ID is not valid
     */
    @Operation(summary = "Get cryptocurrency price given cryptocurrencyID")
    @GetMapping("/cryptocurrencies/{cryptocurrencyId}/price")
    public double getCryptocurrencyPrice(
            @PathVariable(value = "cryptocurrencyId") final String cryptocurrencyId)
            throws ResourceNotFoundException {
        return cryptocurrencyService.getCryptocurrencyPrice(cryptocurrencyId);
    }

    /**
     * Update a cryptocurrency's price.
     *
     * @param cryptocurrencyId CryptocurrencyID
     * @param price            New price
     * @return Updated cryptocurrency
     * @throws ResourceNotFoundException if cryptocurrency ID is not valid
     */
    @Operation(summary = "Update cryptocurrency price given cryptocurrencyID and price")
    @PutMapping("/cryptocurrencies/{cryptocurrencyId}/{price}")
    public Cryptocurrency updateCryptocurrencyPrice(
            @PathVariable(value = "cryptocurrencyId") final String cryptocurrencyId,
            @PathVariable(value = "price") final Float price) throws ResourceNotFoundException {
        return cryptocurrencyService.updateCryptocurrencyPrice(cryptocurrencyId, price);
    }
}
