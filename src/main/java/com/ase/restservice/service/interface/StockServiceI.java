package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import java.util.List;

/**
 * Interface for Stock service.
 */
public interface StockServiceI {
  /**
   * Creates a stock in the database.
   *
   * @param stock Stock
   * @return Created stock
   */
  Stock createStock(Stock stock);

  /**
   * Retrieve all stocks.
   *
   * @return List of stocks
   */
  List<Stock> listStocks();

  /**
   * Updates a stock in the database.
   *
   * @param stock Stock
   * @return Updated stock
   */
  Stock updateStock(Stock stock);

  /**
   * Deletes a stock in the database.
   *
   * @param stockId StockID
   */
  void deleteStockById(String stockId);

  /**
   * Gets a stock by stockId.
   *
   * @param stockId StockID
   * @return Stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  Stock getStockById(String stockId) throws ResourceNotFoundException;

  /**
   * Updates the price of a stock.
   *
   * @param stockId StockID
   * @param price Stock price
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  Stock updateStockPrice(String stockId, Float price) throws ResourceNotFoundException;
}
