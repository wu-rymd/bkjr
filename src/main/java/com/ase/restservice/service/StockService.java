package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * @throws ResourceAlreadyExistsException if stock already exists
   */
  public Stock createStock(Stock stock) throws ResourceAlreadyExistsException {
    if (stockRepository.existsById(stock.getStockId())) {
      throw new ResourceAlreadyExistsException(
          "Stock with ID " + stock.getStockId() + " already exists");
    }
    return stockRepository.save(stock);
  }

  /**
   * Updates a stock in the database.
   *
   * @param stock Stock
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist
   */
  public Stock updateStock(Stock stock) throws ResourceNotFoundException {
    if (!stockRepository.existsById(stock.getStockId())) {
      throw new ResourceNotFoundException(
          "Stock with ID " + stock.getStockId() + " does not exist");
    }
    return stockRepository.save(stock);
  }

  /**
   * Deletes a stock in the database.
   *
   * @param stockId StockID
   * @throws ResourceNotFoundException if stock does not exist
   */
  public void deleteStockById(String stockId) throws ResourceNotFoundException {
    if (!stockRepository.existsById(stockId)) {
      throw new ResourceNotFoundException(
          "Stock with ID " + stockId + " does not exist");
    }
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
            "Stock not found for stockId :: " + stockId));
  }

  /**
   * Updates the price of a stock.
   *
   * @param stockId StockID
   * @param price   Stock price
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
