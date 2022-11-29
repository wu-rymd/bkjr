package com.ase.restservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Collection;

@Entity
@Table(name = "client")
public class Client implements UserDetails {

  private String clientId;
  private String password;

  public Client() {

  }
  /**
   * Represents an account.
   *
   * @param clientId       ID of a client
   * @param password        password for authorization
   */
  public Client(final String clientId, final String password) {
    this.clientId = clientId;
    this.password = password;
  }
  /**
   * Getter for clientId.
   * @return clientId
   */
  @Id
  public String getClientId() {
    return clientId;
  }
  /**
   * Setter for clientId.
   * @param clientId clientId
   */
  public void setClientId(final String clientId) {
    this.clientId = clientId;
  }
  /**
   * Getter for password.
   * @return password
   */
  @Column(name = "password", nullable = false)
  public String getPassword() {
    return password;
  }
  /**
   * Setter for password.
   * @param password password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Required for spring security.
   * @return
   */
  @Transient
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  /**
   * Required for spring security.
   * Get username which is clientID.
   * @return
   */
  @Transient
  @Override
  public String getUsername() {
    return this.getClientId();
  }

  /**
   * Required for spring security.
   * @return
   */
  @Transient
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Required for spring security.
   * @return
   */
  @Transient
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  /**
   * Required for spring security.
   * Probably checks if token is expired or not.
   * @return
   */
  @Transient
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Required for spring security.
   * @return
   */
  @Transient
  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * Custom toString method.
   * @return string representation of client
   */
  @Override
  public String toString() {
    return "Account [clientId=" + getClientId() + "]";
  }
}
