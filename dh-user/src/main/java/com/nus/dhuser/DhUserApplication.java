package com.nus.dhuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.nus.dhmodel.pojo")
public class DhUserApplication {

  public static void main(String[] args) {
    SpringApplication.run(DhUserApplication.class, args);
  }

}
