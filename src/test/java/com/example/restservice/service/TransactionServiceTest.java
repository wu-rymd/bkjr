package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.TransactionRepository;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.StockService;
import com.ase.restservice.service.TransactionService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class TransactionServiceTest {
    @Mock
    private AssetService mockAssetService;
    @Mock
    private AccountService mockAccountService;
    @Mock
    private StockService mockStockService;
    @Mock
    private TransactionRepository mockTransactionRepository;
    @InjectMocks
    private TransactionService transactionService;
    private Transaction buyTransaction;
    private Transaction sellTransaction;
    private Asset asset;
    private Stock stock;
    private String accountId = "jlamborn";
    private String tradableType = "stock";

    @BeforeEach
    // Generate fake transactions
    public void setup() {
        buyTransaction = new Transaction(
                this.accountId,
                this.tradableType,
                "AMZN",
                12.34f,
                "BUY",
                "PENDING");
        sellTransaction = new Transaction(
                this.accountId,
                this.tradableType,
                "AMZN",
                23.45f,
                "SELL",
                "PENDING");
        asset = new Asset(accountId, tradableType, "AMZN", 1234.5f);
        stock = new Stock("AMZN", 3.33f);
    }

    @DisplayName("Test for successful buyTransaction")
    @Test
    public void buyTransactionSuccess() throws Exception {
        // create expected updated asset
        Asset resultAssetTruth = new Asset(
                accountId,
                tradableType,
                "AMZN",
                asset.getQuantity() + buyTransaction.getQuantity());
        // mock asset service to return updated asset
        doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
                asset.getAccountId(),
                asset.getTradableType(),
                asset.getTradableId(),
                buyTransaction.getQuantity());
        doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
        Asset resultAsset = transactionService.buyTransaction(buyTransaction);
        // check accountBalance was called with the correct update amount
        verify(mockAccountService).updateAccountBalance(accountId,
                -1 * (stock.getPrice() * buyTransaction.getQuantity()));
        buyTransaction.setTransactionStatus("COMPLETED");
        // verify that transaction was updated to have transaction status "COMPLETED"
        verify(mockTransactionRepository).save(buyTransaction);
        assertEquals(resultAsset, resultAssetTruth);
    }

    @DisplayName("Test for successful sellTransaction in which the entire asset is not sold")
    @Test
    public void sellTransactionOfPartialAsset() throws Exception {
        // create expected updated asset
        Optional<Asset> resultAssetTruth = Optional.of(new Asset(
                accountId,
                tradableType,
                "AMZN",
                asset.getQuantity() - buyTransaction.getQuantity()));

        // mock asset service to return updated asset
        doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
                asset.getAccountId(),
                asset.getTradableType(),
                asset.getTradableId(),
                sellTransaction.getQuantity());
        doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
        Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransaction);
        // check accountBalance was called with the correct update amount
        verify(mockAccountService).updateAccountBalance(accountId,
                (stock.getPrice() * sellTransaction.getQuantity()));
        sellTransaction.setTransactionStatus("COMPLETED");
        // verify that transaction was updated to have transaction status "COMPLETED"
        verify(mockTransactionRepository).save(sellTransaction);
        assertEquals(resultAsset.get(), resultAssetTruth.get());
    }

    @DisplayName("Test for successful sellTransaction in which the entire asset is "
            + "sold (asset should be deleted from the database")
    @Test
    public void sellTransactionOfEntireAsset() throws Exception {
        doReturn(Optional.empty()).when(mockAssetService).sellAsset(
                asset.getAccountId(),
                asset.getTradableType(),
                asset.getTradableId(),
                sellTransaction.getQuantity());
        doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
        Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransaction);
        verify(mockAccountService).updateAccountBalance(accountId,
                (stock.getPrice() * sellTransaction.getQuantity()));
        sellTransaction.setTransactionStatus("COMPLETED");
        // verify that transaction was updated to have transaction status "COMPLETED"
        verify(mockTransactionRepository).save(sellTransaction);
        // sellTransaction should return null when the asset is deleted
        assertFalse(resultAsset.isPresent());
    }
}
