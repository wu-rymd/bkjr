package com.ase.restservice.auth;

import javax.validation.constraints.NotNull;


public class AuthRequest {
  @NotNull
  private String clientId;

  @NotNull
  private String password;

  /**
   * get clientId.
   * @return clientId
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * sets clientId.
   * @param clientId
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * gets password.
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * sets password.
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }


}

