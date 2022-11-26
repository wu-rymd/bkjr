package com.ase.restservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

/**
 * Represents Account table.
 */
@Entity
@Table(name = "account")
public class Account{

  private String accountId;
  private String password;
  private Float balance;
  private Float startingBalance;

  /**
   * Default constructor for Account.
   */
  public Account() {
  }

  /**
   * Represents an account.
   *
   * @param accountId       ID of an account
   * @param password        password for authorization
   * @param balance         Account balance
   * @param startingBalance Starting balance of an account
   */
  public Account(final String accountId, final String password, final Float balance, final Float startingBalance) {
    this.accountId = accountId;
    this.password = password;
    this.balance = balance;
    this.startingBalance = startingBalance;
  }

  /**
   * Getter for accountId.
   * @return accountId
   */
  @Id
  public String getAccountId() {
    return accountId;
  }

  /**
   * Setter for accountId.
   * @param accountId accountId
   */
  public void setAccountId(final String accountId) {
    this.accountId = accountId;
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
   * Getter for balance.
   * @return balance
   */
  @Column(name = "balance", nullable = false)
  public Float getBalance() {
    return balance;
  }

  /**
   * Setter for balance.
   * @param balance balance
   */
  public void setBalance(final Float balance) {
    this.balance = balance;
  }

  /**
   * Getter for starting balance.
   * @return starting balance
   */
  @Column(name = "starting_balance", nullable = false)
  public Float getStartingBalance() {
    return startingBalance;
  }

  /**
   * Setter for starting balance.
   * @param startingBalance starting balance
   */
  public void setStartingBalance(final Float startingBalance) {
    this.startingBalance = startingBalance;
  }

  /**
   * Custom toString method.
   *
   * @return string representation of account
   */
  @Override
  public String toString() {
    return "Account [accountId=" + accountId + ", balance=" + balance
        + ", startingBalance=" + startingBalance + "]";
  }
}
