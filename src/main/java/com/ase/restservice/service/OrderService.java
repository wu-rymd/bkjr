package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Order;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.OrderRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Order operations.
 */
@Service
public class OrderService {
  @Autowired
  OrderRepository orderRepository;
  @Autowired AssetService assetService;
  @Autowired StockService stockService;
  @Autowired
  AccountService accountService;

  public void createNewOrder(Order order) {
    orderRepository.save(order);
  }
  public void updateOrderStatus(Order order, String status) {
    order.setOrderStatus(status);
    orderRepository.save(order);
  }
  public Optional<Asset> executeOrder(Order order) throws Exception {
    Stock stock = stockService.findById(order.getStockId());
    if (Objects.equals(order.getOrderType(), "BUY")) {
      return Optional.of(buyOrder(order, stock));
    }
    else if (Objects.equals(order.getOrderType(), "Sell")) {
      return sellOrder(order, stock);
    }
    else {
      throw new Exception("INVALID ORDER TYPE");
    }
  }
  public Asset buyOrder(Order order, Stock stock) throws ResourceNotFoundException {
    // UPDATE/CREATE ASSET
    Asset new_asset = assetService.buyAsset(
        order.getAccountId(),
        order.getStockId(),
        order.getNumShares()
    );
    float total_cost = stock.getPrice() * order.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // send the (-) amount of total_cost so that the account service DECREASES the account's balance
    accountService.updateAccountBalance(order.getAccountId(), -total_cost);
    updateOrderStatus(order, "COMPLETED");
    return new_asset;
  }
  public Optional<Asset> sellOrder(Order order, Stock stock) throws Exception {
    // UPDATE/DELETE ASSET
    Optional<Asset> new_asset = assetService.sellAsset(
        order.getAccountId(),
        stock.getStockId(),
        order.getNumShares());
    float total_cost = stock.getPrice() * order.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // SEND THE (+) amount of total_cost so tht the account service INCREASES account's balance
    accountService.updateAccountBalance(order.getAccountId(), total_cost);
    updateOrderStatus(order, "COMPLETED");
    return new_asset;
  }
}
