package com.example.restservice.service;

import com.ase.restservice.exception.AccountNotFoundException;
import com.ase.restservice.exception.InvalidTransactionException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.model.NFT;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.CryptocurrencyService;
import com.ase.restservice.service.NFTService;
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
  @Mock
  private NFTService mockNFTService;
  @Mock
  private CryptocurrencyService mockCryptoService;
  @InjectMocks
  private AssetService assetService;

  private Float startingBalance;
  private Float endingBalance;
  private Float totalValueTruth;
  private Float pnlTruth;
  private Account user;

  private NFT nft;
  private Stock stock;
  private Cryptocurrency crypto;
  private List<Asset> assets = new ArrayList<>();
  private List<Stock> stocks = new ArrayList<>();
  private String accountId = "testAccount";
  private Float portfolioValueTruth;

  @BeforeEach
  // Generate fake stock data, fake asset data
  public void setup() {
    stock = new Stock("AMZN", 10f);
    crypto = new Cryptocurrency("poocoin", 1.5F);
    nft = new NFT("monke", 10.3F);

    assets.add(new Asset(accountId, "stock", "AMZN", 1f));
    assets.add(new Asset(accountId, "cryptocurrency", "poocoin", 2f));
    assets.add(new Asset(accountId, "nft", "monke", 1.8f));



    user = new Account(accountId, 50f, 100f, "binance");
    endingBalance = 50f;
    startingBalance = 100f;
    portfolioValueTruth = stock.getPrice() * assets.get(0).getQuantity()
        + crypto.getPrice() * assets.get(1).getQuantity()
        + nft.getPrice() * assets.get(2).getQuantity();
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

    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(nft).when(mockNFTService).getNFTById(nft.getNftId());
    doReturn(crypto).when(mockCryptoService).getCryptocurrencyById(crypto.getCryptocurrencyId());

    doReturn(assets).when(mockAssetRepository).findAllAssetsByAccountId(accountId);
    Float portfolioValue = assetService.getAccountPortfolioValue(accountId);
    assertEquals(portfolioValue, portfolioValueTruth);
  }

  @DisplayName("JUnit test for buyAsset when asset already exists for account")
  @Test
  public void buyExistingAsset() throws AccountNotFoundException, ResourceNotFoundException {
    Asset asset = assets.get(0);
    Float buyAmount = 12.34f;
    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());
    Asset retAssetTruth = new Asset(
        asset.getAccountId(),
        asset.getTradableType(),
        asset.getTradableId(),
        asset.getQuantity() + buyAmount);
    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, "stock", stock.getStockId(),
        buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for buyAsset when user does not already own shares of asset")
  @Test
  public void buyNewAsset() throws AccountNotFoundException, ResourceNotFoundException {
    String stockId = stock.getStockId();
    Float buyAmount = 12.34f;

    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());

    Asset retAssetTruth = new Asset(
        accountId,
        "stock",
        stockId,
        buyAmount);
    doReturn(Optional.empty()).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, "stock", stockId, buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for buyAsset when stock id does not exist")
  @Test
  public void invalidBuyAsset() throws AccountNotFoundException, ResourceNotFoundException {
    String stockId = stock.getStockId();
    Float buyAmount = 12.34f;

    // buyAsset() will update the asset in the database
    // need to mock this behavior
    doReturn(null).when(mockAssetRepository).save(any());

    Asset retAssetTruth = new Asset(
        accountId,
        "stock",
        stockId,
        buyAmount);
    doReturn(Optional.empty()).when(mockAssetRepository).findById(any());
    Asset retAsset = assetService.buyAsset(accountId, "stock", stockId, buyAmount);
    assertEquals(retAsset, retAssetTruth);
  }

  @DisplayName("JUnit test for sellAsset when asset exists "
      + "for user & user is not selling all of the asset")
  @Test
  public void sellPartialAsset() throws Exception {
    Asset asset = assets.get(0);
    Float sellAmount = .9f;
    Asset updatedAssetTruth = new Asset(accountId, "stock", stock.getStockId(),
        (asset.getQuantity() - sellAmount));

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(
        new AssetId(accountId, "stock", stock.getStockId()));
    doReturn(true).when(mockAssetRepository).existsById(
        new AssetId(accountId, "stock", stock.getStockId()));
    Optional<Asset> updatedAsset = assetService.sellAsset(
        accountId, "stock", stock.getStockId(), sellAmount);
    assertEquals(updatedAsset.get(), updatedAssetTruth);
  }

  @DisplayName("JUnit test for sellAsset when asset exists for "
      + "user and user is selling ALL OF the asset")
  @Test
  public void sellAllAsset() throws Exception {
    Asset asset = assets.get(0);
    Float sellAmount = asset.getQuantity();

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(new AssetId(
        accountId, "stock", stock.getStockId()));
    Optional<Asset> updatedAsset = assetService.sellAsset(
        accountId, "stock", stock.getStockId(), sellAmount);
    // Expected behavior is that sellAsset returns null when the asset is deleted
    assertFalse(updatedAsset.isPresent());
    // Confirm that deleteById was called
    verify(mockAssetRepository).deleteById(new AssetId(accountId, "stock",
        stock.getStockId()));
  }

  @DisplayName("JUnit test for sellAsset when an account tries to sell more of "
      + "the asset than they own")
  @Test
  public void sellAssetInvalidAmount() {
    Asset asset = assets.get(0);
    Float sellAmount = 100f; // an amount of shares > then stock.numShares()

    doReturn(Optional.of(asset)).when(mockAssetRepository).findById(
        new AssetId(accountId, "stock", stock.getStockId()));

    Exception exception = assertThrows(InvalidTransactionException.class, () -> {
      assetService.sellAsset(accountId, "stock", stock.getStockId(), sellAmount);
    });
    String expectedMessage = "Insufficient amount of asset to sell";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @DisplayName("JUnit test for sellAsset when an account tries to sell "
      + "an asst that they do not own")
  @Test
  public void invalidSellAsset() {
    Float sellAmount = 1.23f;
    doReturn(Optional.empty()).when(mockAssetRepository).findById(
        new AssetId(accountId, "stock", stock.getStockId()));
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      assetService.sellAsset(accountId, "stock", stock.getStockId(), sellAmount);
    });
    String expectedMessage = "Asset of the tradable type"
        + "stock" + "with the id " + stock.getStockId()
        + " does not exist for the account: "
        + accountId;
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @DisplayName("JUnit test for getAccountTotalValue success")
  @Test
  public void testAccountTotalValueSuccess()
      throws AccountNotFoundException, ResourceNotFoundException {
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(nft).when(mockNFTService).getNFTById(nft.getNftId());
    doReturn(crypto).when(mockCryptoService).getCryptocurrencyById(crypto.getCryptocurrencyId());


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
    doReturn(stock).when(mockStockService).getStockById(stock.getStockId());
    doReturn(nft).when(mockNFTService).getNFTById(nft.getNftId());
    doReturn(crypto).when(mockCryptoService).getCryptocurrencyById(crypto.getCryptocurrencyId());

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

  @DisplayName("Test successful createAsset")
  @Test
  public void createAssetSuccess() throws ResourceAlreadyExistsException {
    Asset asset = assets.get(0);
    doReturn(false).when(mockAssetRepository).existsById(asset.getAssetId());
    doReturn(asset).when(mockAssetRepository).save(asset);
    assetService.createAsset(asset);
  }

  @DisplayName("test createAsset when asset already exists")
  @Test
  public void createAssetAlreadyExists() {
    Asset asset = assets.get(0);
    doReturn(true).when(mockAssetRepository).existsById(asset.getAssetId());
    ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
        () -> {
      assetService.createAsset(asset);
    });
    String expectedMessage = "Asset already exists";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("Test updateAsset when asset does not exist")
  @Test
  public void updateAssetDNE() {
    Asset asset = assets.get(0);
    doReturn(false).when(mockAssetRepository).existsById(asset.getAssetId());
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      assetService.updateAsset(asset);
    });
    String expectedMessage = "Asset does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("Test deleteAssetByID when asset DNE")
  @Test
  public void deleteAssetByIDDNE() {
    Asset asset = assets.get(0);
    doReturn(Optional.empty()).when(mockAssetRepository).findById(asset.getAssetId());
    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
      assetService.deleteAssetById(asset.getAssetId());
    });
    String expectedMessage = "Asset " + asset.getAssetId() + " does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }
}
