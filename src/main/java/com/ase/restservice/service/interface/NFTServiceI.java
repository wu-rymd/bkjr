package com.ase.restservice.service;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.model.NFT;

import java.util.List;

/**
 * Interface for NFT service.
 */
public interface NFTServiceI {
  /**
   * Creates an NFT in the database.
   *
   * @param nft NFT
   * @return Created NFT
   * @throws ResourceAlreadyExistsException if NFT already exists in the database
   */
  NFT createNFT(NFT nft) throws ResourceAlreadyExistsException;

  /**
   * Retrieve all NFTs.
   *
   * @return List of NFTs
   */
  List<NFT> getAllNFTs();

  /**
   * Updates an NFT in the database.
   *
   * @param nft NFT
   * @return Updated NFT
   * @throws ResourceNotFoundException if NFT does not exist in the database
   */
  NFT updateNFT(NFT nft) throws ResourceNotFoundException;

  /**
   * Deletes an NFT in the database.
   *
   * @param nftId NFTID
   * @throws ResourceNotFoundException if NFT does not exist in the database
   */
  void deleteNFTById(String nftId) throws ResourceNotFoundException;

  /**
   * Gets a NFT by nftId.
   *
   * @param nftId NFTID
   * @return NFT
   * @throws ResourceNotFoundException if NFT does not exist in the database
   */
  NFT getNFTById(String nftId) throws ResourceNotFoundException;

  /**
   * Updates the price of an NFT.
   *
   * @param nftId NFTID
   * @param price NFT price
   * @return Updated NFT
   * @throws ResourceNotFoundException if NFT does not exist in the database
   */
  NFT updateNFTPrice(String nftId, double price) throws ResourceNotFoundException;

  /**
   * Gets the price of an NFT by nftId.
   *
   * @param nftId NFTID
   * @return NFT price
   * @throws ResourceNotFoundException if NFT does not exist in the database
   */
  Float getNFTPrice(String nftId) throws ResourceNotFoundException;
}
