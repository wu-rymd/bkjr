package com.ase.restservice;

import com.ase.restservice.model.Client;
import com.ase.restservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

  @Autowired
  private ClientRepository clientRepository;

  /**
   * Finds client based on clientID.
   * @param clientID clientID
   * @return client
   * @throws UsernameNotFoundException
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String clientID) throws UsernameNotFoundException {
    Client client = clientRepository.findClientId(clientID)
            .orElseThrow(() ->
                    new UsernameNotFoundException("User Not Found with username: " + clientID));

    return client;
  }

}
