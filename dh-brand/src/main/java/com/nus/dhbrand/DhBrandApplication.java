package com.nus.dhbrand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.nus.dhbrand.Mapper")
@SpringBootApplication
public class DhBrandApplication {

  public static void main(String[] args) {
    SpringApplication.run(DhBrandApplication.class, args);
  }

}
