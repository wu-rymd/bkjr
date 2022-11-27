package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidOrderTypeException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.TransactionRepository;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.StockService;
import com.ase.restservice.service.TransactionService;
import java.util.ArrayList;
import java.util.List;
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
  private TransactionRepository mockTransactionRepository;
  @Mock
  private StockService mockStockService;
  @InjectMocks
  private TransactionService transactionService;
  private Transaction buyTransaction;
  private Transaction sellTransaction;
  private Asset asset;
  private Stock stock;
  private String accountId = "jlamborn";
  @BeforeEach
  // Generate fake transactions
  public void setup() {
    buyTransaction = new Transaction(
        this.accountId,
        "AMZN",
        12.34f,
        "BUY",
        "PENDING"
        );
    sellTransaction = new Transaction(
        this.accountId,
        "AMZN",
        23.45f,
        "Sell",
        "PENDING"
    );
    asset = new Asset(accountId, "AMZN", 1234.5f);
    stock = new Stock("AMZN", 3.33f);
  }

  @DisplayName("Test that createTransaction saves incoming transactions")
  @Test
  public void createTransactionSaves()
      throws InvalidOrderTypeException, InvalidTransactionException, ResourceNotFoundException,
      AccountNotFoundException {
   // return value here is not important, we are not testing this portion of the method
    doReturn(asset).when(mockAssetService).buyAsset(
        asset.getAccountId(),
        asset.getStockId(),
        buyTransaction.getNumShares()
    );
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    transactionService.createTransaction(buyTransaction);
    // Check that save is called twice, because the transaction is saved when it is
    // requested and also when it is executed
    verify(mockTransactionRepository, times(2)).save(buyTransaction);
  }

  @DisplayName("Test that updateTransactionStatus saves the updated transaction")
  @Test
  public void updateTransactionStatusSaves() {
    transactionService.updateTransactionStatus(buyTransaction, "COMPLETED");
    verify(mockTransactionRepository).save(buyTransaction);
  }

  @DisplayName("Test for successful buyTransaction")
  @Test
  public void buyTransactionSuccess() throws Exception {
    // create expected updated asset
    Asset resultAssetTruth = new Asset(
        accountId,
        "AMZN",
        asset.getNumShares() + buyTransaction.getNumShares());
    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
        asset.getAccountId(),
        asset.getStockId(),
        buyTransaction.getNumShares()
        );

    Asset resultAsset = transactionService.buyTransaction(buyTransaction, stock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * (stock.getPrice() * buyTransaction.getNumShares()));
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
        "AMZN",
        asset.getNumShares() - buyTransaction.getNumShares()));

    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
        asset.getAccountId(),
        asset.getStockId(),
        sellTransaction.getNumShares()
    );
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransaction, stock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellTransaction.getNumShares()));
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
        asset.getStockId(),
        sellTransaction.getNumShares()
    );
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransaction, stock);
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellTransaction.getNumShares()));
    sellTransaction.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(sellTransaction);
    // sellTransaction should return null when the asset is deleted
    assertFalse(resultAsset.isPresent());
  }

  @DisplayName("Test successful buy order on executeTransaction")
  @Test
  public void executeTransactionBuySuccess()
      throws ResourceNotFoundException, InvalidOrderTypeException, InvalidTransactionException,
      AccountNotFoundException {

    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(asset).when(mockAssetService).buyAsset(
        accountId, buyTransaction.getStockId(),
        buyTransaction.getNumShares()
    );
    transactionService.executeTransaction(buyTransaction);
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * stock.getPrice() * buyTransaction.getNumShares());
    buyTransaction.setTransactionStatus("COMPLETED");
    verify(mockTransactionRepository).save(buyTransaction);
  }

  @DisplayName("Test successful sell order on executeTransaction")
  @Test
  public void executeTransactionSellSuccess()
      throws ResourceNotFoundException, InvalidTransactionException, AccountNotFoundException,
      InvalidOrderTypeException {
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(Optional.of(asset)).when(mockAssetService).sellAsset(
        accountId, stock.getStockId(), sellTransaction.getNumShares());
    sellTransaction.setTransactionStatus("COMPLETED");

    transactionService.executeTransaction(sellTransaction);

    verify(mockAccountService).updateAccountBalance(accountId,
        stock.getPrice() * sellTransaction.getNumShares());
    verify(mockTransactionRepository).save(sellTransaction);
  }

  @DisplayName("Test invalid order type on executeTransaction")
  @Test
  public void executeTransactionInvalidType() throws ResourceNotFoundException {

    Transaction invalidTransaction = new Transaction(this.accountId,
        "AMZN",
        12.34f,
        "KaiserSHMARRN",
        "PENDING");
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    Exception exception = assertThrows(InvalidOrderTypeException.class, () -> {
      transactionService.executeTransaction(invalidTransaction);
    });
    String expectedMessage = "Invalid order type :: " + invalidTransaction.getTransactionType();
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @DisplayName("Test successful listAccountTransactions")
  @Test
  public void listAccountTransactionsSuccess() throws AccountNotFoundException {
    ArrayList<Transaction> accountTransactions = new ArrayList<>();
    accountTransactions.add(buyTransaction);
    accountTransactions.add(sellTransaction);
    doReturn(Optional.of(accountTransactions))
        .when(mockTransactionRepository).findByAccountId(accountId);

    List<Transaction> res = transactionService.listAccountTransactions(accountId);

    assertEquals(accountTransactions, res);
  }

  @DisplayName("Test listAccountTransactions when account does not exist")
  @Test
  public void listAccountTransactionsAccountDNE() {
    doReturn(Optional.empty())
        .when(mockTransactionRepository).findByAccountId(accountId);

    Exception exception = assertThrows(AccountNotFoundException.class, () -> {
      transactionService.listAccountTransactions(accountId);
    });
    String expectedMessage = "Account not found for accountId :: " + accountId;
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }
}
