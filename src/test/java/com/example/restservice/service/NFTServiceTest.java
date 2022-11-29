package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.NFT;
import com.ase.restservice.repository.NFTRepository;
import com.ase.restservice.service.NFTService;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public final class NFTServiceTest {

  @Mock
  private NFTRepository mockNFTRepository;
  @InjectMocks
  private NFTService nftService;

  private NFT nft;
  @BeforeEach
  public void setup() {
    nft = new NFT("DOGE", 10F);
  }

  @DisplayName("Test createNFT success")
  @Test
  public void createNFTSuccess() throws ResourceAlreadyExistsException {
    doReturn(false).when(mockNFTRepository).existsById(nft.getNftId());
    doReturn(nft).when(mockNFTRepository).save(nft);
    nftService.createNFT(nft);
  }

  @DisplayName("Test createNFT when NFT already exists")
  @Test
  public void createNFTAlreadyExists() {
    doReturn(true).when(mockNFTRepository).existsById(nft.getNftId());
    Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
      nftService.createNFT(nft);
    });
    String expectedMessage = "NFT already exists";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("Test getAllNFTs")
  @Test
  public void getAllNFTs() {
    ArrayList<NFT> allNFTs = new ArrayList<>();
    allNFTs.add(nft);
    doReturn(allNFTs).when(mockNFTRepository).findAll();
    nftService.getAllNFTs();
  }

  @DisplayName("Test updateNFT success")
  @Test
  public void updateNFTSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockNFTRepository).existsById(nft.getNftId());
    doReturn(nft).when(mockNFTRepository).save(nft);
    nftService.updateNFT(nft);
  }

  @DisplayName("Test updateNFT when NFT does not exist")
  @Test
  public void updateNFTDNE() {
    doReturn(false).when(mockNFTRepository).existsById(nft.getNftId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      nftService.updateNFT(nft);
    });
    String expectedMessage = "NFT does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test deleteNFT success")
  @Test
  public void deleteNFTSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockNFTRepository).existsById(nft.getNftId());
    nftService.deleteNFTById(nft.getNftId());
    verify(mockNFTRepository).deleteById(nft.getNftId());
  }

  @DisplayName("Test deleteNFT when NFT does not exist")
  @Test
  public void deleteNFTDNE() {
    doReturn(false).when(mockNFTRepository).existsById(nft.getNftId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      nftService.deleteNFTById(nft.getNftId());
    });
    String expectedMessage = "NFT does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test getNFTByID success")
  @Test
  public void getNFTByIDSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockNFTRepository).existsById(nft.getNftId());
    doReturn(Optional.of(nft)).when(mockNFTRepository).findById(nft.getNftId());
    nftService.getNFTById(nft.getNftId());
  }

  @DisplayName("Test getNFTByID when NFT does not exist")
  @Test
  public void getNFTByIDDNE() {
    doReturn(false).when(mockNFTRepository).existsById(nft.getNftId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      nftService.getNFTById(nft.getNftId());
    });
    String expectedMessage = "NFT does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test updateNFTPrice success")
  @Test
  public void updateNFTPriceSucces() throws ResourceNotFoundException {
    doReturn(true).when(mockNFTRepository).existsById(nft.getNftId());
    doReturn(Optional.of(nft)).when(mockNFTRepository).findById(nft.getNftId());
    doReturn(nft).when(mockNFTRepository).save(any());
    NFT res = nftService.updateNFTPrice(nft.getNftId(), 11F);

    assertEquals(nft, res);
  }

  @DisplayName("Test updateNFTPrice when NFT does not exist")
  @Test
  public void updateNFTPriceDNE() {
    doReturn(false).when(mockNFTRepository).existsById(nft.getNftId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      nftService.updateNFTPrice(nft.getNftId(), 10F);
    });
    String expectedMessage = "NFT does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }
}
