package com.ase.restservice.auth;

import javax.validation.constraints.NotNull;


public class AuthRequest {
//  @NotNull @Email @Length(min = 5, max = 50)
  @NotNull
  private String clientId;

//  @NotNull @Length(min = 5, max = 10)
  @NotNull
  private String password;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


}

