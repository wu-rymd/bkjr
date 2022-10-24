package com.ase.restservice.repository;

import com.ase.restservice.model.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Interface for querying the Order table.
 */

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
