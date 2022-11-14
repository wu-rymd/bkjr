package com.ase.restservice.repository;

import com.ase.restservice.model.Transaction;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interface for querying the Transaction table.
 */

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  @Query(value = "SELECT * FROM Transaction WHERE transaction.account_id = ?1", nativeQuery = true)
  List<Transaction> findByAccountId(String accountId);
}
