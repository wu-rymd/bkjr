package com.ase.restservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

    private String accountId;
    private Float balance;
    private Float starting_balance;

    public Account() {

    }

    public Account(String accountId, Float balance,Float starting_balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.starting_balance = starting_balance;
    }

    @Id
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Column(name = "balance", nullable = false)
    public Float getBalance() {
        return balance;
    }
    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Column(name = "starting_balance", nullable = false)
    public Float getStartingBalance() {
        return starting_balance;
    }
    //Not sure if this should exist.
    public void setStartingBalance(Float startingBalance) {
        this.starting_balance = startingBalance;
    }
    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", balance=" + balance + "]";
    }
}