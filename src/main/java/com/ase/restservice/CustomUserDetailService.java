package com.ase.restservice;

import com.ase.restservice.model.Account;
import com.ase.restservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired
  AccountRepository accountRepository;

//  @Override
//  public UserDetails loadUserByUsername(String accountID) throws UsernameNotFoundException {
//    final UserEntity customer = accountRepository.findById(accountID);
//    if (customer == null) {
//      throw new UsernameNotFoundException(email);
//    }
//    UserDetails user = User.withUsername(customer.getEmail())
//            .password(customer.getPassword())
//            .authorities("USER").build();
//    return user;
//  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String accountID) throws UsernameNotFoundException {
    Account account = accountRepository.findAccountId(accountID)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + accountID));

    return account;
  }

}