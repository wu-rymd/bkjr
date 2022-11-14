package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents Account table.
 */
@Entity
@Table(name = "account")
public class Account {

  private String accountId;
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
   * @param balance         Account balance
   * @param startingBalance Starting balance of an account
   */
  public Account(final String accountId, final Float balance, final Float startingBalance) {
    this.accountId = accountId;
    this.balance = balance;
    this.balance = startingBalance;
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
   * @return string representation of account
   */
  @Override
  public String toString() {
    return "Account [accountId=" + accountId + ", balance=" + balance
        + ", startingBalance=" + startingBalance + "]";
  }
}
