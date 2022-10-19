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

    public Account() {

    }

    public Account(String accountId, Float balance) {
        this.accountId = accountId;
        this.balance = balance;
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

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", balance=" + balance + "]";
    }
}