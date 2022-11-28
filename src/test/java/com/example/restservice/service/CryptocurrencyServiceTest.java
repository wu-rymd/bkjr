package com.example.restservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Cryptocurrency;
import com.ase.restservice.repository.CryptocurrencyRepository;
import com.ase.restservice.service.CryptocurrencyService;
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
public final class CryptocurrencyServiceTest {

  @Mock
  private CryptocurrencyRepository mockCryptocurrencyRepository;
  @InjectMocks
  private CryptocurrencyService cryptocurrencyService;

  private Cryptocurrency crypto;
  @BeforeEach
  public void setup() {
    crypto = new Cryptocurrency("DOGE", 10F);
  }

  @DisplayName("Test createCryptocurrency success")
  @Test
  public void createCryptocurrencySuccess() throws ResourceAlreadyExistsException {
    doReturn(false).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    doReturn(crypto).when(mockCryptocurrencyRepository).save(crypto);
    cryptocurrencyService.createCryptocurrency(crypto);
  }

  @DisplayName("Test createCryptocurrency when cryptocurrency already exists")
  @Test
  public void createCryptocurrencyAlreadyExists() {
    doReturn(true).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    Exception exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
      cryptocurrencyService.createCryptocurrency(crypto);
    });
    String expectedMessage = "Cryptocurrency already exists";
    String actualMessage = exception.getMessage();
    assertEquals(actualMessage, expectedMessage);
  }

  @DisplayName("Test getAllCryptoCurrencies")
  @Test
  public void getAllCryptocurrencies() {
    ArrayList<Cryptocurrency> allCrypto = new ArrayList<>();
    allCrypto.add(crypto);
    doReturn(allCrypto).when(mockCryptocurrencyRepository).findAll();
    cryptocurrencyService.getAllCryptocurrencies();
  }

  @DisplayName("Test updateCryptocurrency success")
  @Test
  public void updateCryptocurrencySuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    doReturn(crypto).when(mockCryptocurrencyRepository).save(crypto);
    cryptocurrencyService.updateCryptocurrency(crypto);
  }

  @DisplayName("Test updateCryptocurrency when cryptocurrency does not exist")
  @Test
  public void updateCryptocurrencyDNE() {
    doReturn(false).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      cryptocurrencyService.updateCryptocurrency(crypto);
    });
    String expectedMessage = "Cryptocurrency does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test deleteCryptocurrency success")
  @Test
  public void deleteCryptocurrencySuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    cryptocurrencyService.deleteCryptocurrencyById(crypto.getCryptocurrencyId());
    verify(mockCryptocurrencyRepository).deleteById(crypto.getCryptocurrencyId());
  }

  @DisplayName("Test deleteCryptocurrency when Cryptocurrency does not exist")
  @Test
  public void deleteCryptocurrencyDNE() {
    doReturn(false).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      cryptocurrencyService.deleteCryptocurrencyById(crypto.getCryptocurrencyId());
    });
    String expectedMessage = "Cryptocurrency does not exist";
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test getCryptocurrencyByID success")
  @Test
  public void getCryptocurrencyByIDSuccess() throws ResourceNotFoundException {
    doReturn(Optional.of(crypto)).when(mockCryptocurrencyRepository)
        .findById(crypto.getCryptocurrencyId());
    cryptocurrencyService.getCryptocurrencyById(crypto.getCryptocurrencyId());
  }

  @DisplayName("Test getCryptocurrencyByID when Cryptocurrency does not exist")
  @Test
  public void getCryptocurrencyByIDDNE() {
    doReturn(Optional.empty()).when(mockCryptocurrencyRepository)
        .findById(crypto.getCryptocurrencyId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      cryptocurrencyService.getCryptocurrencyById(crypto.getCryptocurrencyId());
    });
    String expectedMessage = "Cryptocurrency not found for this id :: "
        + crypto.getCryptocurrencyId();
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }

  @DisplayName("Test updateCryptocurrencyPrice success")
  @Test
  public void updateCryptocurrencyPriceSuccess() throws ResourceNotFoundException {
    doReturn(true).when(mockCryptocurrencyRepository)
        .existsById(crypto.getCryptocurrencyId());
    doReturn(Optional.of(crypto)).when(mockCryptocurrencyRepository)
        .findById(crypto.getCryptocurrencyId());
    doReturn(crypto).when(mockCryptocurrencyRepository).save(any());
    Cryptocurrency res = cryptocurrencyService
        .updateCryptocurrencyPrice(crypto.getCryptocurrencyId(), 11F);

    assertEquals(crypto, res);
  }

  @DisplayName("Test updateCryptocurrencyPrice when Cryptocurrency does not exist")
  @Test
  public void updateCryptocurrencyPriceDNE() {
    doReturn(Optional.empty()).when(mockCryptocurrencyRepository)
        .findById(crypto.getCryptocurrencyId());
    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      cryptocurrencyService.updateCryptocurrencyPrice(crypto.getCryptocurrencyId(), 10F);
    });
    String expectedMessage = "Cryptocurrency not found for this id :: "
        + crypto.getCryptocurrencyId();
    String actualMessage = exception.getMessage();
    assertEquals(expectedMessage, actualMessage);
  }
}
