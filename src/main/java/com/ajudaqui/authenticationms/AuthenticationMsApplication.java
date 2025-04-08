package com.ajudaqui.authenticationms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class AuthenticationMsApplication {

  public static void main(String[] args) {
    // http://localhost:8082/swagger-ui.html
    SpringApplication.run(AuthenticationMsApplication.class, args);
  }

}
