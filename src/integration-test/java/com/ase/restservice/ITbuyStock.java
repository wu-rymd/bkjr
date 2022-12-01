package com.ase.restservice;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.model.Stock;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.repository.StockRepository;
import com.ase.restservice.repository.TransactionRepository;
import com.ase.restservice.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ITbuyStock {

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

  @DisplayName("Integration test between controller, service, and repo layers to verify sending a"
      + " buy transaction to the service results in a new asset being created, transaction being"
      + " stored and the transaction status being updated. ")
  @Test
  void buyStockWorksThroughAllLayers() throws Exception {
    Account testAccount = new Account(
        "43ad9cea-f",
        100F,
        100F,
        "binance");

    if (accountRepository.existsById(testAccount.getAccountId())) {
      accountRepository.deleteById(testAccount.getAccountId());
    }
    accountRepository.save(testAccount);

    Stock testStock = new Stock("ce07363b-f", 13.44F);
    if (stockRepository.existsById(testStock.getStockId())) {
      stockRepository.deleteById(testStock.getStockId());
    }
    stockRepository.save(testStock);

    Transaction buyTransaction = new Transaction(
        testAccount.getAccountId(),
        "stock",
        testStock.getStockId(),
        1F,
        "BUY",
        "PENDING"
        );

    mockMvc.perform(post("/transactions")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(buyTransaction)))
            .andExpect(status().isOk());

    // Get logged transaction from db
    Optional<List<Transaction>> transactions =
        transactionRepository.findByAccountId(testAccount.getAccountId());

    assertTrue(transactions.isPresent());
    assertEquals("COMPLETED", transactions.get().get(0).getTransactionStatus());

    AssetId expectedAssetId = new AssetId(
        testAccount.getAccountId(),"stock", testStock.getStockId());

    // Get new asset from database
    Optional<Asset> asset = assetRepository.findById(expectedAssetId);
    assertTrue(asset.isPresent());

    // Cleanup, delete the new asset, transaction
    assetRepository.deleteById(expectedAssetId);
    transactionRepository.deleteById(transactions.get().get(0).getUuid());
  }

  @Test
  public void ITSellStock() throws Exception {
    Account testAccount = new Account(
        "43ad9cea-f",
        100F,
        100F,
        "binance");

    if (accountRepository.existsById(testAccount.getAccountId())) {
      accountRepository.deleteById(testAccount.getAccountId());
    }
    accountRepository.save(testAccount);

    Stock testStock = new Stock("ce07363b-f", 13.44F);
    if (stockRepository.existsById(testStock.getStockId())) {
      stockRepository.deleteById(testStock.getStockId());
    }
    stockRepository.save(testStock);

    Asset userAsset = new Asset(testAccount.getAccountId(),
        "stock", testStock.getStockId(), 1000F);
    assetRepository.save(userAsset);

    Transaction sellTransaction = new Transaction(
        testAccount.getAccountId(),
        "stock",
        testStock.getStockId(),
        1F,
        "SELL",
        "PENDING"
    );
    mockMvc.perform(post("/transactions")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(sellTransaction)))
        .andExpect(status().isOk());

    // Get logged transaction from db
    Optional<List<Transaction>> transactions =
        transactionRepository.findByAccountId(testAccount.getAccountId());

    assertTrue(transactions.isPresent());
    assertEquals("COMPLETED", transactions.get().get(0).getTransactionStatus());

    AssetId expectedAssetId = new AssetId(
        testAccount.getAccountId(),"stock", testStock.getStockId());

    // Get new asset from database
    Optional<Asset> asset = assetRepository.findById(expectedAssetId);
    assertTrue(asset.isPresent());
    Float expectedQuantity = userAsset.getQuantity() - sellTransaction.getQuantity();
    assertEquals(expectedQuantity, asset.get().getQuantity());

    // Cleanup, delete the new asset
    assetRepository.deleteById(expectedAssetId);
    transactionRepository.deleteById(transactions.get().get(0).getUuid());

  }




}
