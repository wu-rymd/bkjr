package com.ase.restservice.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Service for Finance operations (involving Yahoo! Finance API)
 */
@Service
public class FinanceService implements FinanceServiceI {
    /**
     * Checks whether a stock ID is valid.
     *
     * @param stockId Stock ID
     * @return boolean true or false
     */
    public boolean isStockIdValid(String stockId) {
        try {
            Stock apiStock = YahooFinance.get(stockId);
            if (apiStock == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
