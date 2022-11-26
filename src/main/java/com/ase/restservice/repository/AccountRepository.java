package com.ase.restservice.repository;

import com.ase.restservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface for querying the Account table.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

}
