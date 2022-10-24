package com.ase.restservice.repository;

import com.ase.restservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface for querying the Stock table.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, String> {

}