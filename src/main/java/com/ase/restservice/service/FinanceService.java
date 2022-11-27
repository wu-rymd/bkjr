package com.ase.restservice.service;

import com.ase.restservice.exception.InvalidStockIDException;
import com.ase.restservice.model.Stock;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.YahooFinance;

/**
 * Service for Finance operations (involving Yahoo! Finance API)
 */
@Service
public class FinanceService implements FinanceServiceI {
    @Autowired
    private StockService stockService;

    /**
     * Checks whether a stock ID is valid.
     *
     * @param stockId Stock ID
     * @return boolean true or false
     */
    public boolean isStockIdValid(String stockId) {
        try {
            yahoofinance.Stock apiStock = YahooFinance.get(stockId);
            if (apiStock == null) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the price for a given stock ID.
     *
     * @param stockId Stock ID
     * @return Float real-time value of the stock
     * @throws InvalidStockIDException if the stock ID is invalid
     * @throws IOException when there is a connection error
     */
    public Float getStockPrice(String stockId) throws InvalidStockIDException, IOException {
        // Check if the stock ID is valid
        if (!isStockIdValid(stockId)) {
            throw new InvalidStockIDException(
                "Stock ID given is not valid :: " + stockId
            );
        }

        // Get real-time price
        try {
            yahoofinance.Stock apiStock = YahooFinance.get(stockId);
            Float apiPrice = apiStock.getQuote().getPrice().floatValue();
            return apiPrice;
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Creates a Stock object in the database with the current real-time price given a stock ID.
     *
     * @param stockId Stock ID
     * @return Instantiated Stock object with current real-time price
     * @throws InvalidStockIDException if the stock ID is invalid
     * @throws IOException when there is a connection error
     */
    public Stock createStockFromId(String stockId) throws InvalidStockIDException, IOException {
        try {
            Float apiPrice = getStockPrice(stockId);
            Stock stockObj = new Stock(stockId, apiPrice);
            return stockService.createStock(stockObj);
        } catch (InvalidStockIDException e) {
            throw new InvalidStockIDException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Serve historical data from Yahoo! Finance API
     *
     * @param stockId Stock ID to get historical data of
     * @return A list of historical quotes of the stock
     * @throws InvalidStockIDException if the stock ID is invalid
     * @throws IOException when there is a connection error
     */
    public List<HistoricalQuote> getHistorical(final String stockId)
            throws InvalidStockIDException, IOException {
        try {
            Float apiPrice = getStockPrice(stockId); //dummy check for valid stock ID
            yahoofinance.Stock apiStock = YahooFinance.get(stockId);
            List<HistoricalQuote> histQuotes = apiStock.getHistory();
            return histQuotes;
        } catch (InvalidStockIDException e) {
            throw new InvalidStockIDException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
