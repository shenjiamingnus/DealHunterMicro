package com.nus.dhemail;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableRabbit
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DhEmailApplication {

  public static void main(String[] args) {
    SpringApplication.run(DhEmailApplication.class, args);
  }

}
