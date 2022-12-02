package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.ase.restservice.exception.InvalidStockIDException;
import com.ase.restservice.service.FinanceService;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import yahoofinance.YahooFinance;
import yahoofinance.Stock;
import yahoofinance.quotes.stock.StockQuote;


@ExtendWith(MockitoExtension.class)
public final class FinanceServiceTest {

  @InjectMocks
  private FinanceService financeService;

  @Mock
  private YahooFinance mockYahooFinance;
  @Mock
  private StockQuote mockStockQuote;
  @Mock
  private Stock mockYahooStock;
  private final String stockId = "AMZN";

  @BeforeEach
  public void setup() {
//    mockYahooStock = new Stock("AMZN");
  }

  @DisplayName("Test isStockValid when stock is valid")
  @Test
  public void isStockValidWithValidStock() throws IOException {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {
      mockStatic.when(() -> YahooFinance.get(stockId)).thenReturn(mockYahooStock);
      assertTrue(financeService.isStockIdValid(stockId));
    }
  }

  @DisplayName("Test isStockValid when stock is not valid")
  @Test
  public void isStockValidWithInvalidStock() {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {
      mockStatic.when(() -> YahooFinance.get(stockId)).thenReturn(null);
      assertFalse(financeService.isStockIdValid(stockId));
    }
  }

  @DisplayName("Test isStockValid when IOException is thrown")
  @Test
  public void isStockValidIOException() {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {

      mockStatic.when(() -> YahooFinance.get(stockId)).thenThrow(new IOException(""));

      assertFalse(financeService.isStockIdValid(stockId));
    }
  }

  @DisplayName("Test getStockPrice success")
  @Test
  public void getStockPriceSuccess() throws IOException, InvalidStockIDException {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {

      mockStatic.when(() -> YahooFinance.get(stockId)).thenReturn(mockYahooStock);
      doReturn(mockStockQuote).when(mockYahooStock).getQuote();
      doReturn(new BigDecimal(10)).when(mockStockQuote).getPrice();
      Float res = financeService.getStockPrice(stockId);
      assertEquals(10F, res);
    }
  }
  @DisplayName("Test getStockPrice stockID is not valid")
  @Test
  public void getStockPriceStockInvalid() {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {
      mockStatic.when(() -> YahooFinance.get(stockId)).thenReturn(null);

      Exception exception = assertThrows(InvalidStockIDException.class, () -> {
        financeService.getStockPrice(stockId);
      });
      String expectedMessage = "Stock ID given is not valid :: " + stockId;
      assertEquals(expectedMessage, exception.getMessage());
    }
  }

  @DisplayName("Test getStockPrice IOException")
  @Test
  public void getStockPriceIOException() {
    try (MockedStatic<YahooFinance> mockStatic = Mockito.mockStatic(YahooFinance.class)) {

      mockStatic.when(() -> YahooFinance.get(stockId))
          .thenReturn(mockYahooStock)
          .thenThrow(new IOException(""));

      Exception exception = assertThrows(IOException.class, () -> {
        financeService.getStockPrice(stockId);
      });
      String expectedMessage = "java.io.IOException: ";
      assertEquals(expectedMessage, exception.getMessage());
    }
  }
}
