package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yahoofinance.YahooFinance;

/**
 * Service for Stock operations.
 */
@Service
public class StockService implements StockServiceI {

  @Autowired
  private StockRepository stockRepository;

  /**
   * Creates a stock in the database.
   *
   * @param stock Stock
   * @return Created stock
   * @throws ResourceNotFoundException if the stock ID is invalid
   */
  public Stock createStock(Stock stock) throws ResourceNotFoundException {
    // TODO: Throw exception if stock already exists

    // check if stock ID is valid
    String stockId = stock.getStockId();
    try {
        yahoofinance.Stock apiStock = YahooFinance.get(stockId);
        if (apiStock == null) {
          throw new ResourceNotFoundException(
            "Stock ID given is not valid :: " + stockId
          );
        }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return stockRepository.save(stock);
  }

  /**
   * Updates a stock in the database.
   *
   * @param stock Stock
   * @return Updated stock
   */
  public Stock updateStock(Stock stock) {
    // TODO: Throw exception if stock does not exist
    return stockRepository.save(stock);
  }

  /**
   * Deletes a stock in the database.
   *
   * @param stockId StockID
   */
  public void deleteStockById(String stockId) {
    // TODO: Throw exception if stock does not exist
    stockRepository.deleteById(stockId);
  }

  /**
   * Gets a stock by stockId.
   *
   * @param stockId StockID
   * @return Stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Stock getStockById(String stockId) throws ResourceNotFoundException {
    return stockRepository.findById(stockId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Stock not found for stockId :: " + stockId
        ));
  }

  /**
   * Updates the price of a stock.
   *
   * @param stockId StockID
   * @param price Stock price
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Stock updateStockPrice(String stockId, Float price) throws ResourceNotFoundException {
    Stock stock = this.getStockById(stockId);
    stock.setPrice(price);
    final Stock updatedStock = this.updateStock(stock);
    return updatedStock;
  }

  /**
   * Retrieve all stocks.
   *
   * @return List of stocks
   */
  public List<Stock> listStocks() {
    return stockRepository.findAll();
  }
}
