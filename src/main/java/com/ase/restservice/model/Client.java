package com.ase.restservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "client")
public class Client implements UserDetails {

  private String roleprefix = "ROLE_"; //used by Spring security internally
  private String clientId;
  private String password;
  private String role;

  public Client() {

  }
  /**
   * Represents an account.
   *
   * @param clientId       ID of a client
   * @param password        password for authorization
   */
  public Client(final String clientId, final String password, final String role) {
    this.clientId = clientId;
    this.password = password;
    this.role = role;
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
  public void setPassword(final String password) { //needs to use the auth thing
    this.password = password;
  }
  /**
   * Getter for role.
   * @return role
   */
  @Column(name = "role", nullable = false)
  public String getRole() {
    return role;
  }
  /**
   * Setter for role.
   * @param role role
   */
  public void setRole(final String role) { //needs to use the auth thing
    this.role = role;
  }

  /**
   * Required for spring security.
   * @return
   */

  @Transient
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
//    return null;
    List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

    list.add(new SimpleGrantedAuthority(roleprefix + role));
//    list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    return list;
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
