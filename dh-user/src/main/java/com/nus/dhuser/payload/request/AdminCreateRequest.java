package com.nus.dhuser.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateRequest {

  @NotBlank
  @Size(max = 50)
  private String username;

  @NotBlank
  @Size(max = 50)
  private String password;

  @NotBlank
  @Size(max = 50)
  private String email;


}
