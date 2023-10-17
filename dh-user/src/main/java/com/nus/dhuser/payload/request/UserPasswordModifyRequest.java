package com.nus.dhuser.payload.request;

import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordModifyRequest {

  @Size(max = 100)
  private String password;

}
