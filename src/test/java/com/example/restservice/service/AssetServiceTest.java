package com.example.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public final class AssetServiceTest {
  @Mock
  private AssetRepository mockAssetRepository;
  @Mock
  private StockService mockStockService;
  @Mock
  private AccountRepository mockAccountRepository;
  @InjectMocks
  private AssetService assetService;

  private Float startingBalance;
  private Float endingBalance;
  private Float totalValueTruth;
  private Float pnlTruth;
  private Account user;
  private List<Asset> assets =  new ArrayList<>();
  private List<Stock> stocks = new ArrayList<>();
  private String accountId = "testAccount";
  private Float portfolioValueTruth;

  @BeforeEach
  // Generate fake stock data, fake asset data
  public void setup() {
    stocks.add(new Stock("AMZN", 103.11f));
    assets.add(new Asset(accountId, "AMZN", 10f));

    stocks.add(new Stock("GOOGL", 111.03f));
    assets.add(new Asset(accountId, "GOOGL", 1.5f));

    stocks.add(new Stock("META", 132.00f));
    assets.add(new Asset(accountId, "META", 10.3f));

    portfolioValueTruth = (103.11f * 10f) + (111.03f * 1.5f) + (132.00f * 10.3f);

    user = new Account(accountId, 50f, 100f);
    endingBalance = 50f;
    startingBalance = 100f;

    totalValueTruth = endingBalance + portfolioValueTruth;
    pnlTruth = (totalValueTruth - startingBalance) / startingBalance;
  }
  @AfterEach
  public void cleanUp() {
    assets = new ArrayList<>();
  }

  @DisplayName("JUnit test for getPortfolioValue")
  @Test
  public void getPortfolioValue()
      throws AccountNotFoundException, ResourceNotFoundException {
    for (Stock stock : stocks) {
      doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    }
    doReturn(assets).when(mockAssetRepository).findAllAssetsByAccountId(accountId);
    Float portfolioValue = assetService.getAccountPortfolioValue(accountId);
    assertEquals(portfolioValue, portfolioValueTruth);
  }

  @DisplayName("JUnit test for buyAsset when asset already exists for account")
  @Test
  public void buyExistingAsset() {
    Stock stock = stocks.get(0);
    Asset asset = assets.get(0);
    Float buyAmount = 12.34f;
    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());
    Asset retAssetTruth = new Asset(
        asset.getAccountId(),
        asset.getStockId(),
        asset.getNumShares() + buyAmount
    );
    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, stock.getStockId(), buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for buyAsset when user does not already own shares of asset")
  @Test
  public void buyNewAsset() {
    String stockId = stocks.get(0).getStockId();
    Float buyAmount = 12.34f;

    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());

    Asset retAssetTruth = new Asset(
        accountId,
        stockId,
        buyAmount);
    doReturn(Optional.empty()).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, stockId, buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for buyAsset when stock id does not exist")
  @Test
  public void invalidBuyAsset() {
    String stockId = stocks.get(0).getStockId();
    Float buyAmount = 12.34f;

    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());

    Asset retAssetTruth = new Asset(
        accountId,
        stockId,
        buyAmount);
    doReturn(Optional.empty()).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, stockId, buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for sellAsset when asset exists "
      + "for user & user is not selling all of the asset")
  @Test
  public void sellPartialAsset() throws Exception {
    Stock stock = stocks.get(0);
    Asset asset = assets.get(0);
    Float sellAmount = .9f;
    Asset updatedAssetTruth = new Asset(accountId, stock.getStockId(), (
        asset.getNumShares() - sellAmount));

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(
        new AssetId(accountId, stock.getStockId()));

    Optional<Asset> updatedAsset = assetService.sellAsset(
        accountId, stock.getStockId(), sellAmount);
    assertEquals(updatedAsset.get(), updatedAssetTruth);
  }

  @DisplayName("JUnit test for sellAsset when asset exists for "
      + "user and user is selling ALL OF the asset")
  @Test
  public void sellAllAsset() throws Exception {
    Stock stock = stocks.get(0);
    Asset asset = assets.get(0);
    Float sellAmount = asset.getNumShares();

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(new AssetId(
        accountId, stock.getStockId()));
    Optional<Asset> updatedAsset = assetService.sellAsset(
        accountId, stock.getStockId(), sellAmount);
    // Expected behavior is that sellAsset returns null when the asset is deleted
    assertFalse(updatedAsset.isPresent());
    // Confirm that deleteById was called
    verify(mockAssetRepository).deleteById(new AssetId(accountId, stock.getStockId()));
  }

  @DisplayName("JUnit test for sellAsset when an account tries to sell more of "
      + "the asset than they own")
  @Test
  public void sellAssetInvalidAmount()  {
    Stock stock = stocks.get(0);
    Asset asset = assets.get(0);
    Float sellAmount = 100f;  // an amount of shares > then stock.numShares()

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(
        new AssetId(accountId, stock.getStockId()));

    Exception exception = assertThrows(InvalidTransactionException.class, () -> {
      assetService.sellAsset(accountId, stock.getStockId(), sellAmount);
    });
    String expectedMessage = "Insufficient shares";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @DisplayName("JUnit test for sellAsset when an account tries to sell "
      + "an asst that they do not own")
  @Test
  public void invalidSellAsset() {
    Stock stock = stocks.get(0);
    Float sellAmount = 1.23f;
    doReturn(Optional.empty()).when(mockAssetRepository).findById(
        new AssetId(accountId, stock.getStockId()));
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      assetService.sellAsset(accountId, stock.getStockId(), sellAmount);
    });
    String expectedMessage = "Asset " + stock.getStockId()
        + " does not exist for user " + accountId;
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
  @DisplayName("JUnit test for getAccountTotalValue success")
  @Test
  public void testAccountTotalValueSuccess()
      throws AccountNotFoundException, ResourceNotFoundException {
    for (Stock stock : stocks) {
      doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    }

    doReturn(assets).when(mockAssetRepository).findAllAssetsByAccountId(accountId);
    doReturn(Optional.ofNullable(user)).when(mockAccountRepository).findById(accountId);
    Float totalValue = assetService.getAccountTotalValue(accountId);
    assertEquals(totalValue, totalValueTruth);
  }

  @DisplayName("Test for getAccountTotalValue failure")
  @Test
  public void testAccountTotalValueFailure() throws AccountNotFoundException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(accountId);
    AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
      assetService.getAccountTotalValue(accountId);
    });
    String expectedMessage = "Account not found for accountId :: " + accountId;
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
  @DisplayName("JUnit test for getAccountPnl success")
  @Test
  public void testAccountPnlSuccess()
      throws AccountNotFoundException, ResourceNotFoundException {
    for (Stock stock : stocks) {
      doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    }
    doReturn(assets).when(mockAssetRepository).findAllAssetsByAccountId(accountId);
    doReturn(Optional.ofNullable(user)).when(mockAccountRepository).findById(accountId);
    Float pnl = assetService.getAccountPnl(accountId);
    assertEquals(pnl, pnlTruth);
  }
  @DisplayName("Test for getAccountPnl() when account does not exist")
  @Test
  public void testAccountPnlFailure() throws AccountNotFoundException {
    doReturn(Optional.empty()).when(mockAccountRepository).findById(accountId);
    AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
      assetService.getAccountPnl(accountId);
    });
    String expectedMessage = "Account not found for accountId :: " + accountId;
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }
}
