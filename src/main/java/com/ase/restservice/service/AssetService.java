package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.Account;
import com.ase.restservice.model.Asset;
import com.ase.restservice.model.AssetId;
import com.ase.restservice.repository.AccountRepository;
import com.ase.restservice.repository.AssetRepository;
import com.ase.restservice.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private StockRepository stockRepository;    // call the service instead when it's created?
    
    public List<Asset> getAssetsByAccountId(String accountId) {
        return assetRepository.findAllAssetsByAccountId(accountId);
    }
    public Float getAccountPortfolioValue(String accountId) throws ResourceNotFoundException{
        List<Asset> userAssets = this.getAssetsByAccountId(accountId);
        float total = 0f;
        for (Asset asset: userAssets) {
            // Total value of a given asset is the current share price * the # of shares the account owns
            total+= stockRepository.findById(asset.getStockId()).orElseThrow(() ->
                            new ResourceNotFoundException("Stock not found for stock id ::" + asset.getStockId()))
                    .getPrice() * asset.getNumShares();
        }
        return total;
    }

    /**
     *
     * @param accountId UUID for which account this transaction belongs to
     * @param stockId UUID for which stock is being bought
     * @param numShares number of shares request in the buy order
     */
    public Asset buyAsset(String accountId, String stockId, Float numShares) {
        // when buying an asset, first check if it already exists.
        // If exists, then update the stock amount
        // If not exists, write a new asset
        Optional<Asset> asset = assetRepository.findById(new AssetId(accountId, stockId));
        if (asset.isPresent()) {
            // update the current asset
            Asset current_asset = asset.get();
            current_asset.setNumShares(current_asset.getNumShares() + numShares);
            assetRepository.save(current_asset);
            return current_asset;
        }
        else {
            // new asset
            Asset new_asset = new Asset(accountId, stockId, numShares);
            assetRepository.save(new_asset);
            return new_asset;
        }
    }
}
