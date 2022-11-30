package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.StockRepository;
import com.ase.restservice.service.StockService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class StockServiceTest {

  @Mock
  private StockRepository mockStockRepository;
  @InjectMocks
  private StockService stockService;
  private Stock stock;

  @BeforeEach
  public void setup() {
    stock = new Stock("AMZN", 3.33f);
  }

  @DisplayName("Test createStock success")
  @Test
  public void createStockSuccess() throws ResourceAlreadyExistsException {
    stockService.createStock(stock);
    verify(mockStockRepository).save(stock);
  }

  @DisplayName("Test createStock when stock already exists")
  @Test
  public void createStockAlreadyExists() {
    doReturn(true).when(mockStockRepository).existsById(stock.getStockId());

    Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
      stockService.createStock(stock);
    });
    String expectedMessage = "Stock with ID " + stock.getStockId() + " already exists";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @DisplayName("Test updateStock success")
  @Test
  public void updateStockSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockStockRepository).existsById(stock.getStockId());
    stockService.updateStock(stock);
    verify(mockStockRepository).save(stock);
  }

  @DisplayName("Test updateStock when stock does not exist")
  @Test
  public void updateStockDNE() {
    doReturn(false).when(mockStockRepository).existsById(stock.getStockId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      stockService.updateStock(stock);
    });
    String expectedMessage = "Stock with ID " + stock.getStockId() + " does not exist";
    assertEquals(expectedMessage, exception.getMessage());
  }

  @DisplayName("Test deleteStockById success")
  @Test
  public void deleteStockByIdSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockStockRepository).existsById(stock.getStockId());

    stockService.deleteStockById(stock.getStockId());

    verify(mockStockRepository).deleteById(stock.getStockId());
  }

  @DisplayName("Test deleteStockById when stock does not exist")
  @Test
  public void deleteStockByIdStockDNE() {
    doReturn(false).when(mockStockRepository).existsById(stock.getStockId());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      stockService.deleteStockById(stock.getStockId());
    });

    String expectedMessage = "Stock with ID AMZN does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test getStockById success")
  @Test
  public void getStockByIdSuccess() throws ResourceNotFoundException {
    doReturn(Optional.of(stock)).when(mockStockRepository).findById(stock.getStockId());
    stockService.getStockById(stock.getStockId());
  }

  @DisplayName("Test updateStockPrice success")
  @Test
  public void updateStockPriceSuccess() throws ResourceNotFoundException {

    Stock updatedStock = new Stock(stock.getStockId(), 10F);
    doReturn(Optional.of(stock)).when(mockStockRepository).findById(stock.getStockId());
    doReturn(updatedStock).when(mockStockRepository).save(any());
    doReturn(true).when(mockStockRepository).existsById(stock.getStockId());
    stockService.updateStockPrice(stock.getStockId(), 10F);
  }

  @DisplayName("Test listStocks success")
  @Test
  public void listStockSuccess() {
    ArrayList<Stock> allStocks = new ArrayList<>();
    allStocks.add(stock);
    doReturn(allStocks).when(mockStockRepository).findAll();

    stockService.listStocks();
  }
}
