package com.ase.restservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ase.restservice.model.NFT;
import com.ase.restservice.repository.NFTRepository;
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
public class ITNFT {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private NFTRepository nftRepository;

  @DisplayName("Verify the updateNFTPrice endpoint works through the controller, "
      + "service and repo layers on success")
  @Test
  public void ITUpdateNFTPriceSuccessWorksThroughAllLayers() throws Exception {
    NFT testNFT = new NFT("c482ba45-7", 100F);

    if (nftRepository.existsById(testNFT.getNftId())) {
      nftRepository.deleteById(testNFT.getNftId());
    }
    nftRepository.save(testNFT);

    mockMvc.perform(put("/nfts/{nftId}",
            testNFT.getNftId())
            .contentType("application/json")
            .content(String.valueOf(50)))
        .andExpect(status().isOk());

    assertTrue(nftRepository.existsById(testNFT.getNftId()));
    Optional<NFT> resNFT =
        nftRepository.findById(testNFT.getNftId());
    assertEquals(50, resNFT.get().getPrice());
  }

  @DisplayName("Verify the updateNFTPRice endpoint works through the controller"
      + "service and repo layers when NFT does not exist")
  @Test
  public void ITUpdateNFTPriceDNEWorksThroughAllLayers() throws Exception {
    NFT testNFT = new NFT("c482ba45-7", 100F);

    if (nftRepository.existsById(testNFT.getNftId())) {
      nftRepository.deleteById(testNFT.getNftId());
    }
    // Now NFT does not exist in database
    mockMvc.perform(put("/nfts/{nftId}",
            testNFT.getNftId())
            .contentType("application/json")
            .content(String.valueOf(50)))
        .andExpect(status().is4xxClientError());
  }
}
