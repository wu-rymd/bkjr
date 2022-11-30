package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidOrderTypeException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.model.NFT;
import com.ase.restservice.model.Transaction;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.TransactionRepository;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.CryptocurrencyService;
import com.ase.restservice.service.NFTService;
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
  private StockService mockStockService;
  @Mock
  private NFTService mockNFTService;
  @Mock
  private CryptocurrencyService mockCryptoservice;
  @Mock
  private TransactionRepository mockTransactionRepository;
  @InjectMocks
  private TransactionService transactionService;
  private Transaction buyTransactionStock;
  private Transaction sellTransactionStock;
  private Transaction buyTransactionCrypto;
  private Transaction sellTransactionCrypto;
  private Transaction buyTransactionNFT;
  private Transaction sellTransactionNFT;

  private Asset stockAsset;
  private Asset cryptoAsset;
  private Asset nftAsset;
  private Stock stock;

  private NFT nft;

  private Cryptocurrency crypto;
  private String accountId = "jlamborn";
  private String tradableType = "stock";

  @BeforeEach
  // Generate fake transactions
  public void setup() {
    buyTransactionStock = new Transaction(
        this.accountId,
        this.tradableType,
        "AMZN",
        12.34f,
        "BUY",
        "PENDING");
    sellTransactionStock = new Transaction(
        this.accountId,
        this.tradableType,
        "AMZN",
        23.45f,
        "SELL",
        "PENDING");

    buyTransactionNFT = new Transaction(
        this.accountId,
        "nft",
        "monke",
        11.11F,
        "BUY",
        "PENDING"
    );
    sellTransactionNFT = new Transaction(
        this.accountId,
        "nft",
        "monke",
        12.11F,
        "SELL",
        "PENDING"
    );
    buyTransactionCrypto = new Transaction(
        this.accountId,
        "cryptocurrency",
        "doge",
        11.11F,
        "BUY",
        "PENDING"
    );
    sellTransactionCrypto = new Transaction(
        this.accountId,
        "cryptocurrency",
        "doge",
        12.11F,
        "SELL",
        "PENDING"
    );

    stockAsset = new Asset(accountId, tradableType, "AMZN", 1234.5f);
    cryptoAsset = new Asset(
        accountId, "cryptocurrency", "doge", 89.1F);
    nftAsset = new Asset(accountId, "nft", "monke", 65.4F);

    stock = new Stock("AMZN", 3.33f);
    nft = new NFT("monke", 3.33f);
    crypto = new Cryptocurrency("doge", 3.33f);
  }

  @DisplayName("Test for successful buyTransaction stock")
  @Test
  public void buyTransactionSuccessStock() throws Exception {
    // create expected updated asset
    Asset resultAssetTruth = new Asset(
        accountId,
        tradableType,
        "AMZN",
        stockAsset.getQuantity() + buyTransactionStock.getQuantity());
    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
        stockAsset.getAccountId(),
        stockAsset.getTradableType(),
        stockAsset.getTradableId(),
        buyTransactionStock.getQuantity());
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    Asset resultAsset = transactionService.buyTransaction(buyTransactionStock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * (stock.getPrice() * buyTransactionStock.getQuantity()));
    buyTransactionStock.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(buyTransactionStock);
    assertEquals(resultAsset, resultAssetTruth);
  }

  @DisplayName("Test for successful buyTransaction NFT")
  @Test
  public void buyTransactionSuccessNFT() throws Exception {
    // create expected updated asset
    Asset resultAssetTruth = new Asset(
        accountId,
        "nft",
        "monke",
        nftAsset.getQuantity() + buyTransactionNFT.getQuantity());
    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
        nftAsset.getAccountId(),
        nftAsset.getTradableType(),
        nftAsset.getTradableId(),
        buyTransactionNFT.getQuantity());
    doReturn(nft).when(mockNFTService).getNFTById(nft.getNftId());
    Asset resultAsset = transactionService.buyTransaction(buyTransactionNFT);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * (nft.getPrice() * buyTransactionNFT.getQuantity()));
    buyTransactionNFT.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(buyTransactionNFT);
    assertEquals(resultAsset, resultAssetTruth);
  }

  @DisplayName("Test for successful buyTransaction Crypto")
  @Test
  public void buyTransactionSuccessCrypto() throws Exception {
    // create expected updated asset
    Asset resultAssetTruth = new Asset(
        accountId,
        "cryptocurrency",
        "doge",
        cryptoAsset.getQuantity() + buyTransactionCrypto.getQuantity());
    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
        cryptoAsset.getAccountId(),
        cryptoAsset.getTradableType(),
        cryptoAsset.getTradableId(),
        buyTransactionCrypto.getQuantity());
    doReturn(crypto).when(mockCryptoservice).getCryptocurrencyById(crypto.getCryptocurrencyId());
    Asset resultAsset = transactionService.buyTransaction(buyTransactionCrypto);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * (crypto.getPrice() * buyTransactionCrypto.getQuantity()));
    buyTransactionCrypto.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(buyTransactionCrypto);
    assertEquals(resultAsset, resultAssetTruth);
  }



  @DisplayName("Test for successful sellTransaction in which the entire asset is not sold STOCK")
  @Test
  public void sellTransactionOfPartialAssetStock() throws Exception {
    // create expected updated asset
    Optional<Asset> resultAssetTruth = Optional.of(new Asset(
        accountId,
        tradableType,
        "AMZN",
        stockAsset.getQuantity() - sellTransactionStock.getQuantity()));

    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
        stockAsset.getAccountId(),
        stockAsset.getTradableType(),
        stockAsset.getTradableId(),
        sellTransactionStock.getQuantity());
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransactionStock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellTransactionStock.getQuantity()));
    sellTransactionStock.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(sellTransactionStock);
    assertEquals(resultAsset.get(), resultAssetTruth.get());
  }

  @DisplayName("Test for successful sellTransaction in which the entire asset is not sold CRYPTO")
  @Test
  public void sellTransactionOfPartialAssetCrypto() throws Exception {
    // create expected updated asset
    Optional<Asset> resultAssetTruth = Optional.of(new Asset(
        accountId,
        "cryptocurrency",
        "doge",
        cryptoAsset.getQuantity() - sellTransactionCrypto.getQuantity()));

    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
        cryptoAsset.getAccountId(),
        cryptoAsset.getTradableType(),
        cryptoAsset.getTradableId(),
        sellTransactionCrypto.getQuantity());
    doReturn(crypto).when(mockCryptoservice).getCryptocurrencyById(crypto.getCryptocurrencyId());
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransactionCrypto);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        (crypto.getPrice() * sellTransactionCrypto.getQuantity()));
    sellTransactionCrypto.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(sellTransactionCrypto);
    assertEquals(resultAsset.get(), resultAssetTruth.get());
  }

  @DisplayName("Test for successful sellTransaction in which the entire asset is not sold CRYPTO")
  @Test
  public void sellTransactionOfPartialAssetNFT() throws Exception {
    // create expected updated asset
    Optional<Asset> resultAssetTruth = Optional.of(new Asset(
        accountId,
        "nft",
        "monke",
        nftAsset.getQuantity() - sellTransactionNFT.getQuantity()));

    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
        nftAsset.getAccountId(),
        nftAsset.getTradableType(),
        nftAsset.getTradableId(),
        sellTransactionNFT.getQuantity());
    doReturn(nft).when(mockNFTService).getNFTById(nft.getNftId());
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransactionNFT);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        (nft.getPrice() * sellTransactionNFT.getQuantity()));
    sellTransactionNFT.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(sellTransactionNFT);
    assertEquals(resultAsset.get(), resultAssetTruth.get());
  }

  @DisplayName("Test for successful sellTransaction in which the entire asset is "
      + "sold (asset should be deleted from the database")
  @Test
  public void sellTransactionOfEntireAsset() throws Exception {
    doReturn(Optional.empty()).when(mockAssetService).sellAsset(
        stockAsset.getAccountId(),
        stockAsset.getTradableType(),
        stockAsset.getTradableId(),
        sellTransactionStock.getQuantity());
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    Optional<Asset> resultAsset = transactionService.sellTransaction(sellTransactionStock);
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellTransactionStock.getQuantity()));
    sellTransactionStock.setTransactionStatus("COMPLETED");
    // verify that transaction was updated to have transaction status "COMPLETED"
    verify(mockTransactionRepository).save(sellTransactionStock);
    // sellTransaction should return null when the asset is deleted
    assertFalse(resultAsset.isPresent());
  }

  @DisplayName("Test successful buy order on executeTransaction")
  @Test
  public void executeTransactionBuySuccess()
      throws ResourceNotFoundException, InvalidOrderTypeException, InvalidTransactionException,
      AccountNotFoundException {

    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(stockAsset).when(mockAssetService).buyAsset(
        accountId,
        buyTransactionStock.getTradableType(),
        buyTransactionStock.getTradableId(),
        buyTransactionStock.getQuantity()
    );
    transactionService.executeTransaction(buyTransactionStock);
    verify(mockAccountService).updateAccountBalance(accountId,
        -1 * stock.getPrice() * buyTransactionStock.getQuantity());
    buyTransactionStock.setTransactionStatus("COMPLETED");
    verify(mockTransactionRepository).save(buyTransactionStock);
  }

  @DisplayName("Test successful sell order on executeTransaction")
  @Test
  public void executeTransactionSellSuccess()
      throws ResourceNotFoundException, InvalidTransactionException, AccountNotFoundException,
      InvalidOrderTypeException {
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(Optional.of(stockAsset)).when(mockAssetService).sellAsset(
        accountId,
        sellTransactionStock.getTradableType(),
        stock.getStockId(), sellTransactionStock.getQuantity());
    sellTransactionStock.setTransactionStatus("COMPLETED");

    transactionService.executeTransaction(sellTransactionStock);

    verify(mockAccountService).updateAccountBalance(accountId,
        stock.getPrice() * sellTransactionStock.getQuantity());
    verify(mockTransactionRepository).save(sellTransactionStock);
  }

  @DisplayName("Test invalid order type on executeTransaction")
  @Test
  public void executeTransactionInvalidType() throws ResourceNotFoundException {

    Transaction invalidTransaction = new Transaction(this.accountId,
        this.tradableType,
        "AMZN",
        12.34f,
        "KaiserSHMARRN",
        "PENDING");
    Exception exception = assertThrows(InvalidOrderTypeException.class, () -> {
      transactionService.executeTransaction(invalidTransaction);
    });
    String expectedMessage = "Invalid transaction type";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("Test successful listAccountTransactions")
  @Test
  public void listAccountTransactionsSuccess() throws AccountNotFoundException {
    ArrayList<Transaction> accountTransactions = new ArrayList<>();
    accountTransactions.add(buyTransactionStock);
    accountTransactions.add(sellTransactionStock);
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
