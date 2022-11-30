package com.ase.restservice.repository;

import com.ase.restservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface for querying the Client table.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
  @Query(value = "SELECT * FROM Client WHERE client.client_id = ?1", nativeQuery = true)
  Optional<Client> findClientId(String clientID);
}
