package com.nus.dhbrand.payload.response;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

  private String accessToken;

  private String tokenType = "Bearer";

  private String username;

  private Integer isAdmin;

  public JwtAuthenticationResponse(String accessToken, String username, Integer isAdmin) {
    this.accessToken = accessToken;
    this.username = username;
    this.isAdmin = isAdmin;
  }
}
