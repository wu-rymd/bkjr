package com.ase.restservice.service;

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
}
