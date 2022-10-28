package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import java.util.List;

/**
 * Interface for Stock service.
 */
public interface StockServiceI {
  Stock createStock(Stock stock);

  List<Stock> listStocks();

  Stock updateStock(Stock stock);

  void deleteStockById(String stockId);

  Stock getStockById(String stockId) throws ResourceNotFoundException;

  Stock updateStockPrice(String stockId, Float price) throws ResourceNotFoundException;
}
