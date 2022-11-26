package com.ase.restservice.auth;

public class AuthResponse {
  private String clientId;
  private String accessToken;

  public AuthResponse() { }

  public AuthResponse(String clientId, String accessToken) {
    this.clientId = clientId;
    this.accessToken = accessToken;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

}