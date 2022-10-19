package com.ase.restservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ase.restservice.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, String>{

}