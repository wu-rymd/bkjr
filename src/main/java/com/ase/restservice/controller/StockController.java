package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /stocks endpoints.
 */
@RestController
public class StockController {
  @Autowired
  private StockRepository stockRepository;

  /**
   * Create a new stock.
   *
   * @param stock Stock
   * @return Updated stock
   */
  @PostMapping("/stocks")
  public Stock createStock(@Valid @RequestBody Stock stock) {
    // TODO: Throw exception if stock already exists
    return stockRepository.save(stock);
  }

  /**
   * Retrieve a stock's price.
   *
   * @param stockId StockID
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @GetMapping("/stocks/{stockId}/price")
  public Stock getStockPrice(@PathVariable(value = "stockId")
      String stockId) throws ResourceNotFoundException {
    Stock stock = stockRepository.findById(stockId)
        .orElseThrow(() -> new ResourceNotFoundException(
          "Stock not found for this id :: " + stockId
        ));
    return stock;
  }

  /**
   * Update stock price.
   *
   * @param stockId StockID
   * @param stockDetails includes stock price
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @PutMapping("/stocks/{stockId}/price")
  public Stock updateStockPrice(@PathVariable(value = "stockId")
      String stockId, @Valid @RequestBody Stock stockDetails) throws ResourceNotFoundException {
    Stock stock = stockRepository.findById(stockId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Stock not found for this id :: " + stockId
        ));
    stock.setPrice(stockDetails.getPrice());
    final Stock updatedStock = stockRepository.save(stock);
    return updatedStock;
  }
}