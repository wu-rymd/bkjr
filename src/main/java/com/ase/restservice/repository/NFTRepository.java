package com.ase.restservice.repository;

import com.ase.restservice.model.NFT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for querying the NFT table.
 */
@Repository
public interface NFTRepository extends JpaRepository<NFT, String> {

}
