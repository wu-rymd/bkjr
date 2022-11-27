package com.ase.restservice.controller;

import com.ase.restservice.exception.InvalidStockIDException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.service.FinanceService;
import com.ase.restservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /finance endpoints.
 */
@RestController
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    @Autowired
    private StockService stockService;

    /**
     * Retrieve a stock's real-time price via Yahoo! Finance API,
     *
     * @param stockId StockID
     * @return Real-time stock price via Yahoo! Finance API
     * @throws ResourceNotFoundException if stock ID is invalid
     */
    @Operation(summary = "Get price of stock given stockId from Yahoo! Finance API")
    @GetMapping("/finance/{stockId}/price")
    public Float getApiStockPrice(@PathVariable(value = "stockId") final String stockId)
        throws ResourceNotFoundException, IOException, InvalidStockIDException {
        return financeService.getStockPrice(stockId);
    }

    /**
     * Update all stock prices in database with real-time price.
     *
     * @return List of stocks and their updated stock prices in the database
     * @throws ResourceNotFoundException if stock ID stored in the database is invalid
     * @throws IOException if connection error to Yahoo! Finance API
     */
    @Operation(summary = "Update all stock prices in database with real-time price")
    @GetMapping("/finance/updateAllStockPrices")
    public List<Stock> updateAllStockPrices()
        throws IOException, InvalidStockIDException, ResourceNotFoundException {
        // get stocks in database
        List<Stock> dbStocks = stockService.listStocks();
        // update each stock in db with real-time price
        for (Stock thisStock : dbStocks) {
            String thisStockId = thisStock.getStockId();
            Float thisStockPrice = financeService.getStockPrice(thisStockId);
            stockService.updateStockPrice(thisStockId, thisStockPrice);
        }
        // show current state of db
        return stockService.listStocks();
    }

    /**
     * Update one stock price in database with real-time price.
     *
     * @param stockId Stock ID of price to be updated
     * @return Updated stock
     * @throws ResourceNotFoundException if stock ID is not in database or invalid
     * @throws IOException if connection error to Yahoo! Finance API
     */
    @Operation(summary = "Update one stock price in database with real-time price")
    @GetMapping("/finance/{stockId}/updatePrice")
    public Stock updateStockPrice(@PathVariable(value = "stockId") final String stockId)
        throws ResourceNotFoundException, IOException, InvalidStockIDException {
        Float stockPrice = financeService.getStockPrice(stockId);
        stockService.updateStockPrice(stockId, stockPrice);
        return stockService.getStockById(stockId);
    }
}
