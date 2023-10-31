package com.nus.dhuser.vo;

import lombok.Data;

@Data
public class JwtVo {

  public Long id;

  public String username;

  public String token;

  public String email;

  public Integer isAdmin;

}
