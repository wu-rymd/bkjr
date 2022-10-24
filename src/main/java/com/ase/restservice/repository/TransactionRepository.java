package com.ase.restservice.repository;

import com.ase.restservice.model.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Interface for querying the Transaction table.
 */

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
