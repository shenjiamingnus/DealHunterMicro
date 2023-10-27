package com.nus.dhproduct;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableRabbit
@EnableFeignClients(basePackages = "com.nus.dhproduct.feign")
@EntityScan(basePackages = "com.nus.dhmodel.pojo")
@SpringBootApplication
public class DhProductApplication {

  public static void main(String[] args) {
    SpringApplication.run(DhProductApplication.class, args);
  }

}
