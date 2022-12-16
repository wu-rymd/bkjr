package com.ase.restservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.repository.StockRepository;
import com.ase.restservice.repository.TransactionRepository;
import com.ase.restservice.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ITStock {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private AssetRepository assetRepository;
  @Autowired
  private AccountService accountService;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private StockRepository stockRepository;
  @Autowired
  private TransactionRepository transactionRepository;

  @DisplayName("Integration test for updateStockPrice endpoint verifying that the "
      + "controller, service and repo layers function correctly together")
  @Test
  public void ITUpdateStockPriceSuccessWorksThroughAllLayers() throws Exception {
    Stock testStock = new Stock("c482ba45-7", 100F);

    if (stockRepository.existsById(testStock.getStockId())) {
      stockRepository.deleteById(testStock.getStockId());
    }
    stockRepository.save(testStock);

    mockMvc.perform(put("/stocks/{stockId}/{stockPrice}",
        testStock.getStockId(), 50))
        .andExpect(status().isOk());

    assertTrue(stockRepository.existsById(testStock.getStockId()));
    Optional<Stock> resStock = stockRepository.findById(testStock.getStockId());
    assertEquals(50, resStock.get().getPrice());

    // clean up
    stockRepository.deleteById(testStock.getStockId());

  }

  @DisplayName("Integration test for updateStockPrice endpoint when stock DNE verifying that "
      + "controller, service and repo layers function correctly together")
  @Test
  public void ITUpdateStockPriceDNEWorksThroughAllLayers() throws Exception {
    Stock testStock = new Stock("c482ba45-7", 100F);

    if (stockRepository.existsById(testStock.getStockId())) {
      stockRepository.deleteById(testStock.getStockId());
    }
    // Now stock does not exist in database
    mockMvc.perform(put("/stocks/{stockId}/{stockPrice}",
            testStock.getStockId(), 50))
        .andExpect(status().is4xxClientError());
  }
}
