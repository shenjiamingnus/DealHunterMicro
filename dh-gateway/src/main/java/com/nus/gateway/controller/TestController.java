package com.nus.gateway.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
public class TestController {

  @GetMapping("/test")
  public Object test() {
    return "app1";
  }

}

