package com.ase.restservice.repository;

import com.ase.restservice.model.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for querying the Cryptocurrency table.
 */
@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {

}
