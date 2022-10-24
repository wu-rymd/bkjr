package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Order;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.OrderRepository;
import com.ase.restservice.service.AccountService;
import com.ase.restservice.service.AssetService;
import com.ase.restservice.service.OrderService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @Mock
  private AssetService mockAssetService;
  @Mock
  private AccountService mockAccountService;
  @Mock
  private OrderRepository mockOrderRepository;
  @InjectMocks
  private OrderService orderService;
  Order buyOrder;
  Order sellOrder;
  Asset asset;
  Stock stock;
  String accountId = "jlamborn";
  @BeforeEach
  // Generate fake orders
  public void setup() {
    buyOrder = new Order(
        this.accountId,
        "AMZN",
        12.34f,
        "BUY",
        "PENDING"
        );
    sellOrder = new Order(
        this.accountId,
        "AMZN",
        23.45f,
        "SELL",
        "PENDING"
    );
    asset = new Asset(accountId, "AMZN", 1234.5f);
    stock = new Stock("AMZN", 3.33f);
  }
  @DisplayName("Test for successful buyOrder")
  @Test
  public void buyOrderSuccess() throws Exception {
    // create expected updated asset
    Asset resultAssetTruth = new Asset(
        accountId,
        "AMZN",
        asset.getNumShares() + buyOrder.getNumShares());
    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).buyAsset(
        asset.getAccountId(),
        asset.getStockId(),
        buyOrder.getNumShares()
        );

    Asset resultAsset = orderService.buyOrder(buyOrder, stock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        -1*(stock.getPrice() * buyOrder.getNumShares()));
    buyOrder.setOrderStatus("COMPLETED");
    // verify that order was updated to have order status "COMPLETED"
    verify(mockOrderRepository).save(buyOrder);
    assertEquals(resultAsset, resultAssetTruth);
  }

  @DisplayName("Test for successful sellOrder in which the entire asset is not sold")
  @Test
  public void sellOrderOfPartialAsset() throws Exception {
    // create expected updated asset
    Optional<Asset> resultAssetTruth = Optional.of(new Asset(
        accountId,
        "AMZN",
        asset.getNumShares() - buyOrder.getNumShares()));

    // mock asset service to return updated asset
    doReturn(resultAssetTruth).when(mockAssetService).sellAsset(
        asset.getAccountId(),
        asset.getStockId(),
        sellOrder.getNumShares()
    );
    Optional<Asset> resultAsset = orderService.sellOrder(sellOrder, stock);
    // check accountBalance was called with the correct update amount
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellOrder.getNumShares()));
    sellOrder.setOrderStatus("COMPLETED");
    // verify that order was updated to have order status "COMPLETED"
    verify(mockOrderRepository).save(sellOrder);
    assertEquals(resultAsset.get(), resultAssetTruth.get());
  }

  @DisplayName("Test for successful sellOrder in which the entire asset is sold (asset should "
      + "be deleted from the database")
  @Test
  public void SellOrderOfEntireAsset() throws Exception {
    doReturn(Optional.empty()).when(mockAssetService).sellAsset(
        asset.getAccountId(),
        asset.getStockId(),
        sellOrder.getNumShares()
    );
    Optional<Asset> resultAsset = orderService.sellOrder(sellOrder, stock);
    verify(mockAccountService).updateAccountBalance(accountId,
        (stock.getPrice() * sellOrder.getNumShares()));
    sellOrder.setOrderStatus("COMPLETED");
    // verify that order was updated to have order status "COMPLETED"
    verify(mockOrderRepository).save(sellOrder);
    // sellOrder should return null when the asset is deleted
    assertFalse(resultAsset.isPresent());
  }
}
