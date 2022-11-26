package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.model.NFT;
import com.ase.restservice.repository.NFTRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for NFT operations.
 */
@Service
public class NFTService {
    @Autowired
    private NFTRepository nftRepository;

    /**
     * Creates an NFT in the database.
     * 
     * @param nft NFT
     * @return Created NFT
     * @throws ResourceNotFoundException if NFT already exists in the database
     */
    public NFT createNFT(NFT nft) throws ResourceNotFoundException {
        if (nftRepository.existsById(nft.getNftId())) {
            throw new ResourceNotFoundException("NFT already exists");
        }
        return nftRepository.save(nft);
    }

    /**
     * Retrieve all NFTs.
     * 
     * @return List of NFTs
     */
    public List<NFT> getAllNFTs() {
        return nftRepository.findAll();
    }

    /**
     * Updates an NFT in the database.
     * 
     * @param nft NFT
     * @return Updated NFT
     * @throws ResourceNotFoundException if NFT does not exist in the database
     */
    public NFT updateNFT(NFT nft) throws ResourceNotFoundException {
        if (!nftRepository.existsById(nft.getNftId())) {
            throw new ResourceNotFoundException("NFT does not exist");
        }
        return nftRepository.save(nft);
    }

    /**
     * Deletes an NFT in the database.
     * 
     * @param nftId NFTID
     * @throws ResourceNotFoundException if NFT does not exist in the database
     */
    public void deleteNFTById(String nftId) throws ResourceNotFoundException {
        if (!nftRepository.existsById(nftId)) {
            throw new ResourceNotFoundException("NFT does not exist");
        }
        nftRepository.deleteById(nftId);
    }

    /**
     * Gets a NFT by nftId.
     * 
     * @param nftId NFTID
     * @return NFT
     * @throws ResourceNotFoundException if NFT does not exist in the database
     */
    public NFT getNFTById(String nftId) throws ResourceNotFoundException {
        if (!nftRepository.existsById(nftId)) {
            throw new ResourceNotFoundException("NFT does not exist");
        }
        return nftRepository.findById(nftId).get();
    }

    /**
     * Updates the price of an NFT.
     * 
     * @param nftId NFTID
     * @param price NFT price
     * @return Updated NFT
     * @throws ResourceNotFoundException if NFT does not exist in the database
     */
    public NFT updateNFTPrice(String nftId, Float price) throws ResourceNotFoundException {
        if (!nftRepository.existsById(nftId)) {
            throw new ResourceNotFoundException("NFT does not exist");
        }
        NFT nft = getNFTById(nftId);
        nft.setPrice(price);
        return updateNFT(nft);
    }

    /**
     * Gets the price of an NFT.
     * 
     * @param nftId NFTID
     * @return NFT price
     * @throws ResourceNotFoundException if NFT does not exist in the database
     */
    public Float getNFTPrice(String nftId) throws ResourceNotFoundException {
        return getNFTById(nftId).getPrice();
    }
}
