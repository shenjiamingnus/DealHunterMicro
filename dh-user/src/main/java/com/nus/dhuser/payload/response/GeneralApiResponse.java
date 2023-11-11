package com.nus.dhuser.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralApiResponse {

  private Boolean success;

  private String message;

  private Object result;

  public GeneralApiResponse(Boolean success, String message) {
    this.success = success;
    this.message = message;
  }


}
