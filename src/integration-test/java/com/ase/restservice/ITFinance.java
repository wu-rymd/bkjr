package com.ase.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ITFinance {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @DisplayName("Integration test verifying that getApiStockPrice endpoint success works through "
      + "controller, service, and Yahoo Finance API layers")
  @Test
  public void getApiStockPriceSuccess() throws Exception {

    mockMvc.perform(get("/finance/{stockId}/price",
            "AMZN"))
        .andExpect(status().isOk());

  }
  @DisplayName("Integration test verifying that getApiStockPrice endpoint on an invalid stock"
      + "works through controller, service and Yahoo Finance API layers")
  @Test
  public void getApiStockPriceInvalidStock() throws Exception {

    String invalidStockID = "thisIsNotARealTicker";
    MvcResult res = mockMvc.perform(get("/finance/{stockId}/price",
            invalidStockID))
        .andExpect(status().is4xxClientError())
        .andReturn();

    String expectedMessage = "Stock ID given is not valid :: " + invalidStockID;

    assertEquals(expectedMessage, Objects.requireNonNull(res.getResolvedException()).getMessage());
  }

}
