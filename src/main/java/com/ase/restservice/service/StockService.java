package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Stock operations.
 */
@Service
public class StockService {

  @Autowired
  private StockRepository stockRepository;

  /**
   * Saves a stock to the database.
   *
   * @param stock Stock
   * @return Updated stock
   */
  public Stock save(Stock stock) {
    // TODO: Throw exception if stock already exists
    return stockRepository.save(stock);
  }

  /**
   * Gets a stock by stockId.
   *
   * @param stockId StockID
   * @return Stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  public Stock findById(String stockId) throws ResourceNotFoundException {
    return stockRepository.findById(stockId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Stock not found for stockId :: " + stockId
        ));
  }

}
