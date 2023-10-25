package com.nus.dhbrand;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@EntityScan(basePackages = "com.nus.dhmodel.pojo")
@SpringBootApplication
public class DhBrandApplication {

  public static void main(String[] args) {
    SpringApplication.run(DhBrandApplication.class, args);
  }

}
