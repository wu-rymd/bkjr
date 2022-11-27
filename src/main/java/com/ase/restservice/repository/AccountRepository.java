package com.ase.restservice.repository;

import com.ase.restservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface for querying the Account table.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  @Query(value = "SELECT * FROM Account WHERE account.client_id = ?1", nativeQuery = true)
  List<Account> findAllAccountByClient(String client);

  @Query(value = "SELECT * FROM Account WHERE account.account_id = ?1", nativeQuery = true)
  Optional<Account> findAccountByAccountId(String accountId);
}
