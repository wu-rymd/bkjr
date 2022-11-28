package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.service.FinanceService;
import com.ase.restservice.service.StockService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /stocks endpoints.
 */
@RestController
public final class StockController {
  @Autowired
  private FinanceService financeService;

  @Autowired
  private StockService stockService;

  /**
   * Create a new stock. Checks if stock ID is valid before storing in database.
   *
   * @param stock Stock
   * @return Updated stock
   */
  @Operation(summary = "Create stock given Stock object")
  @PostMapping("/stocks")
  public Stock createStock(@Valid @RequestBody final Stock stock)
      throws ResourceNotFoundException {
    String stockId = stock.getStockId();
    if (!financeService.isStockIdValid(stockId)) {
      throw new ResourceNotFoundException(
          "Stock ID given is not valid :: " + stockId
      );
    }
    return stockService.createStock(stock);
  }

  /**
   * Endpoint to get  all stocks available in the database.
   *
   * @return a list of all available stocks in database
   */
  @Operation(summary = "List all stocks")
  @GetMapping("/stocks")
  public List<Stock> listStocks() {
    return stockService.listStocks();
  }

  /**
   * Retrieve a stock's price.
   *
   * @param stockId StockID
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "Get price of stock given stockId")
  @GetMapping("/stocks/{stockId}/price")
  public Float getStockPrice(@PathVariable(value = "stockId") final String stockId)
      throws ResourceNotFoundException {
    Stock stock = stockService.getStockById(stockId);
    return stock.getPrice();
  }

  /**
   * Update stock price.
   *
   * @param stockId StockID
   * @param price   Stock price
   * @return Updated stock
   * @throws ResourceNotFoundException if stock does not exist in the database
   */
  @Operation(summary = "Update price of stock given accountId and price")
  @PutMapping("/stocks/{stockId}/{price}")
  public Stock updateStockPrice(@PathVariable(value = "stockId") final String stockId,
                                @PathVariable(value = "price") final Float price)
      throws ResourceNotFoundException {
    return stockService.updateStockPrice(stockId, price);
  }
}
