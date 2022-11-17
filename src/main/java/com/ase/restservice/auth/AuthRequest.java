package com.ase.restservice.auth;

import javax.validation.constraints.NotNull;


public class AuthRequest {
//  @NotNull @Email @Length(min = 5, max = 50)
  @NotNull
  private String accountId;

//  @NotNull @Length(min = 5, max = 10)
  @NotNull
  private String password;

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


}

