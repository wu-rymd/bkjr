package com.ase.restservice.auth;

public class AuthResponse {
  private String accountId;
  private String accessToken;

  public AuthResponse() { }

  public AuthResponse(String accountId, String accessToken) {
    this.accountId = accountId;
    this.accessToken = accessToken;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

}