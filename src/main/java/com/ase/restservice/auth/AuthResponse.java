package com.ase.restservice.auth;

public class AuthResponse {
  private String clientId;
  private String accessToken;

  public AuthResponse() { }

  /**
   * Constructor for AuthResponse.
   * @param clientId
   * @param accessToken
   */
  public AuthResponse(String clientId, String accessToken) {
    this.clientId = clientId;
    this.accessToken = accessToken;
  }

  /**
   * Gets clientId.
   * @return client
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * Sets clientId.
   * @param clientId
   */
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Gets access token.
   * @return access token
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Sets access token.
   * @param accessToken
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

}
