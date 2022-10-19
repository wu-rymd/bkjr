package com.ase.restservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ase.restservice.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{

}