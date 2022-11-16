package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import java.io.IOException;

/**
 * Interface for Finance service (involving Yahoo! Finance API)
 */
public interface FinanceServiceI {
    /**
     * Checks whether a stock ID is valid.
     *
     * @param stockId Stock ID
     * @return boolean true or false
     */
    boolean isStockIdValid(String stockId);

    /**
     * Gets the price for a given stock ID.
     *
     * @param stockId Stock ID
     * @return Float real-time value of the stock
     * @throws ResourceNotFoundException if the stock ID is invalid
     */
    Float getStockPrice(String stockId) throws ResourceNotFoundException, IOException;

    /**
     * Creates a Stock object in the database with the current real-time price given a stock ID.
     *
     * @param stockId Stock ID
     * @return Instantiated Stock object with current real-time price
     * @throws ResourceNotFoundException if the stock ID is invalid
     * @throws IOException when there is a connection error
     */
    Stock createStockFromId(String stockId) throws ResourceNotFoundException, IOException;
}
