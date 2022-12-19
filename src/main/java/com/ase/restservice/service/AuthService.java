//package com.ase.restservice.service;
//
//import com.ase.restservice.exception.ResourceAlreadyExistsException;
//import com.ase.restservice.model.Asset;
//import org.springframework.stereotype.Service;
//
///**
// * Service for Auth operations.
// */
//@Service
//public class AuthService implements com.ase.restservice.service.AuthServiceI {
//
//  /**
//   * Creates an asset in the database.
//   *
//   * @param asset Asset
//   * @return Created asset
//   * @throws ResourceAlreadyExistsException if asset already exists
//   */
//  public Asset createClient(Asset asset) throws ResourceAlreadyExistsException {
//    if (assetRepository.existsById(asset.getAssetId())) {
//      throw new ResourceAlreadyExistsException("Asset already exists");
//    }
//
//
//
//
//    return clientRepository.save(newClient);
//  }
//
//
//
//}
