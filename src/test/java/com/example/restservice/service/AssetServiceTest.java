package com.example.restservice.service;

import com.ase.restservice.controller.AccountController;
import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.Stock;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.repository.StockRepository;
import com.ase.restservice.service.AssetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private StockRepository stockRepository;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AssetService assetService;
    List<Asset> assets =  new ArrayList<>();
    List<Stock> stocks = new ArrayList<>();
    String accountId;
    Float portfolioValueTruth;
    Float startingBalance;
    Float endingBalance;
    Float totalValueTruth;
    Float pnlTruth;

    Account user;
    @BeforeEach
    // Generate fake stock data, fake asset data
    public void setup(){
        stocks.add(new Stock("AMZN", 103.11f));
        assets.add(new Asset(accountId, "AMZN", 10f));

        stocks.add(new Stock("GOOGL", 111.03f));
        assets.add(new Asset(accountId, "GOOGL", 1.5f));

        stocks.add(new Stock("META", 132.00f));
        assets.add(new Asset(accountId, "META", 10.3f));

        portfolioValueTruth = (103.11f*10f) + (111.03f*1.5f) + (132.00f*10.3f);

        user = new Account("kutaykarakas",50f,100f);
        endingBalance = 50f;
        startingBalance = 100f;

        totalValueTruth = endingBalance + portfolioValueTruth;
        pnlTruth = (totalValueTruth - startingBalance)/startingBalance;
        for (Stock stock : stocks) {
            given(stockRepository.findById(stock.getStockId()))
                    .willReturn(Optional.of(stock));
        }
    }
    @AfterEach
    public void cleanUp(){
        assets = new ArrayList<>();
    }

    @DisplayName("JUnit test for getPortfolioValue")
    @Test
    public void getPortfolioValue() {
        given(assetRepository.findAllAssetsByAccountId(accountId))
                .willReturn(assets);
        Float portfolioValue = assetService.getAccountPortfolioValue(accountId);
        assertEquals(portfolioValue, portfolioValueTruth);
    }
    @DisplayName("JUnit test for getAccountTotalValue")
    @Test
    public void testAccountTotalValue() throws ResourceNotFoundException {//throws here is weird


        given(assetRepository.findAllAssetsByAccountId(accountId))
                .willReturn(assets);
        given(accountRepository.findById(accountId))
                .willReturn(Optional.ofNullable(user));

        Float totalValue = assetService.getAccountTotalValue(accountId);

        assertEquals(totalValue, totalValueTruth);
    }
    @DisplayName("JUnit test for getAccountPnl")
    @Test
    public void testAccountPnl() throws ResourceNotFoundException {//throws here is weird
        given(assetRepository.findAllAssetsByAccountId(accountId))
                .willReturn(assets);

        given(accountRepository.findById(accountId))
                .willReturn(Optional.ofNullable(user));

        Float pnl = assetService.getAccountPnl(accountId);
        assertEquals(pnl, pnlTruth);
    }
}
