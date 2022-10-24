package com.ase.restservice.controller;

import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Order;
import com.ase.restservice.service.OrderService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /order endpoint.
 */
@RestController
public class OrderController {
  @Autowired
  OrderService orderService;

  @PostMapping("/order")
  public Optional<Asset> postAsset(@RequestBody Order order) throws Exception {
    return orderService.createNewOrder(order);
  }

}
