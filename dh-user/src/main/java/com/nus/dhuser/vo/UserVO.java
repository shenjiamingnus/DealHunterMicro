package com.nus.dhuser.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserVO implements Serializable {

  private static final Long serialVersionUID = 1L;

  public Long id;

  public String username;
}
