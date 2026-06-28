package com.ajudaqui.porteiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import com.ajudaqui.porteiro.config.PorteiroRuntimeHints;

import io.mongock.runner.springboot.EnableMongock;

@ImportRuntimeHints(PorteiroRuntimeHints.class)
@EnableMongock
@SpringBootApplication
public class PorteiroApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(PorteiroApiApplication.class, args);
  }

}
