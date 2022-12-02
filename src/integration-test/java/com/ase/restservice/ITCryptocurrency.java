package com.ase.restservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.repository.CryptocurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ITCryptocurrency {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private CryptocurrencyRepository cryptocurrencyRepository;

  @DisplayName("Verify the updateCryptocurrency endpoint works through the controller, service, "
      + "and repo layers on success")
  @Test
  public void ITUpdateCryptocurrencyPriceSuccessWorksThroughAllLayers() throws Exception {
    Cryptocurrency testCrypto = new Cryptocurrency("c482ba45-7", 100F);

    if (cryptocurrencyRepository.existsById(testCrypto.getCryptocurrencyId())) {
      cryptocurrencyRepository.deleteById(testCrypto.getCryptocurrencyId());
    }
    cryptocurrencyRepository.save(testCrypto);

    mockMvc.perform(put("/cryptocurrencies/{cryptocurrencyId}/{price}",
            testCrypto.getCryptocurrencyId(), 50))
        .andExpect(status().isOk());

    assertTrue(cryptocurrencyRepository.existsById(testCrypto.getCryptocurrencyId()));
    Optional<Cryptocurrency> resCrypto =
        cryptocurrencyRepository.findById(testCrypto.getCryptocurrencyId());
    assertEquals(50, resCrypto.get().getPrice());
  }

  @DisplayName("Verify the updateCryptocurrencyPrice endpoint works through the controller, "
      + "service and repo layer when cryptocurrency does not exist")
  @Test
  public void ITUpdateCryptoPriceDNEWorksThroughAllLayers() throws Exception {
    Cryptocurrency testCrypto = new Cryptocurrency("c482ba45-7", 100F);

    if (cryptocurrencyRepository.existsById(testCrypto.getCryptocurrencyId())) {
      cryptocurrencyRepository.deleteById(testCrypto.getCryptocurrencyId());
    }
    // Now cryptocurrency does not exist in database
    mockMvc.perform(put("/cryptocurrencies/{cryptocurrencyId}/{price}",
            testCrypto.getCryptocurrencyId(), 50))
        .andExpect(status().is4xxClientError());
  }
}
