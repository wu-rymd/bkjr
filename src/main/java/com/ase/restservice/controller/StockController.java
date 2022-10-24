package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import com.ase.restservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
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
  private StockService stockService;

  /**
   * Create a new stock.
   *
   * @param stock Stock
   * @return Updated stock
   */
  @Operation(summary = "add a new stock to the database with given id and price")
  @PostMapping("/stocks")
  public Stock createStock(@Valid @RequestBody Stock stock) {
    return stockService.save(stock);
  }

  /**
   * Retrieve a stock's price.
   *
   * @param stockId StockID
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "get price of stock with given id")
  @GetMapping("/stocks/{stockId}/price")
  public Float getStockPrice(@PathVariable(value = "stockId") String stockId)
      throws ResourceNotFoundException {
    Stock stock = stockService.findById(stockId);
    return stock.getPrice();
  }

  /**
   * Update stock price.
   *
   * @param stockId StockID
   * @param stockDetails includes stock price
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "update price of stock with given id")
  @PutMapping("/stocks/{stockId}/price")
  public Stock updateStockPrice(@PathVariable(value = "stockId") String stockId,
      @Valid @RequestBody Stock stockDetails) throws ResourceNotFoundException {
    Stock stock = stockService.findById(stockId);
    stock.setPrice(stockDetails.getPrice());
    final Stock updatedStock = stockService.save(stock);
    return updatedStock;
  }
}