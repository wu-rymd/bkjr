package com.ase.restservice.controller;

import com.ase.restservice.exception.ResourceNotFoundException;
import com.ase.restservice.exception.ResourceAlreadyExistsException;
import com.ase.restservice.model.NFT;
import com.ase.restservice.service.NFTService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for /nfts endpoints.
 */
@RestController
public final class NFTController {

  @Autowired
  private NFTService nftService;

  /**
   * Create a new NFT. Checks if NFT ID is valid before
   * storing in database.
   *
   * @param nft NFT
   * @return Updated NFT
   */
  @Operation(summary = "Create NFT given NFT object")
  @PostMapping("/nfts")
  public NFT createNFT(@Valid @RequestBody final NFT nft)
      throws ResourceAlreadyExistsException {
    return nftService.createNFT(nft);
  }

  /**
   * Endpoint to get all NFTs available in the database.
   *
   * @return a list of all available NFTs in database
   */
  @Operation(summary = "List all NFTs")
  @GetMapping("/nfts")
  public List<NFT> listNFTs() {
    return nftService.getAllNFTs();
  }

  /**
   * Retrieve a NFT's price.
   *
   * @param nftId NFTID
   * @return NFT price
   * @throws ResourceNotFoundException if NFT ID is not valid
   */
  @Operation(summary = "Get NFT price given nftID")
  @GetMapping("/nfts/{nftId}")
  public Float getNFTPrice(@PathVariable final String nftId)
      throws ResourceNotFoundException {
    return nftService.getNFTPrice(nftId);
  }

  /**
   * Update a NFT's price.
   *
   * @param nftId NFTID
   * @param price New price
   * @return Updated NFT
   * @throws ResourceNotFoundException if NFT ID is not valid
   */
  @Operation(summary = "Update NFT price given nftID and new price")
  @PutMapping("/nfts/{nftId}")
  public NFT updateNFTPrice(@PathVariable final String nftId,
                            @Valid @RequestBody final Float price)
      throws ResourceNotFoundException {
    return nftService.updateNFTPrice(nftId, price);
  }

}
