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

  /**
   * Executes orders to buy/sell assets. Directs to helper methods based on order type.
   *
   * @param order Order object placed
   * @return return the updated asset unless the asset was deleted (in the case the user sold
   *        all the shares of the asset), then return null.
   *
   * @throws Exception when order is invalid, or required resources do not exist in database
   */
  public Optional<Asset> executeOrder(Order order) throws Exception {
    Stock stock = stockService.findById(order.getStockId());
    if (Objects.equals(order.getOrderType(), "BUY")) {
      return Optional.of(buyOrder(order, stock));
    } else if (Objects.equals(order.getOrderType(), "Sell")) {
      return sellOrder(order, stock);
    } else {
      throw new Exception("INVALID ORDER TYPE");
    }
  }

  /**
   * Executes buy orders by doing the following: Updating/creating account asset,
   * updating account balance, updating order status.
   *
   * @param order Order object to be executed, with orderType="BUY"
   * @param stock Stock to be bought
   * @return account's updated asset after the buyOrder has been executed
   * @throws ResourceNotFoundException if account does not exist
   */
  public Asset buyOrder(Order order, Stock stock) throws ResourceNotFoundException {
    // UPDATE/CREATE ASSET
    Asset newAsset = assetService.buyAsset(
        order.getAccountId(),
        order.getStockId(),
        order.getNumShares()
    );
    float totalCost = stock.getPrice() * order.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // send the (-) amount of total_cost so that the account service DECREASES the account's balance
    accountService.updateAccountBalance(order.getAccountId(), -totalCost);
    updateOrderStatus(order, "COMPLETED");
    return newAsset;
  }

  /**
   *Executes sell order by doing the following: Updating/deleting account asset,
   * updating account balance, updating order status.
   *
   * @param order Order object to be executed, with orderType="SELL"
   * @param stock Stock to be sold
   * @return account's updated asset after sellOrder has been excecuted, return null in
   *        the case that all the asset has been sold (asset has been deleted)
   * @throws Exception if invalid sell, or required resources do not exist
   */
  public Optional<Asset> sellOrder(Order order, Stock stock) throws Exception {
    // UPDATE/DELETE ASSET
    Optional<Asset> newAsset = assetService.sellAsset(
        order.getAccountId(),
        stock.getStockId(),
        order.getNumShares());
    float totalCost = stock.getPrice() * order.getNumShares();
    // UPDATE ACCOUNT BALANCE
    // SEND THE (+) amount of total_cost so tht the account service INCREASES account's balance
    accountService.updateAccountBalance(order.getAccountId(), totalCost);
    updateOrderStatus(order, "COMPLETED");
    return newAsset;
  }
}
