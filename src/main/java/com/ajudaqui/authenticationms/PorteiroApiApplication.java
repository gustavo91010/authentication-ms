package com.ajudaqui.authenticationms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.mongock.runner.springboot.EnableMongock;

@EnableMongock
@SpringBootApplication
public class PorteiroApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(PorteiroApiApplication.class, args);
  }

}
