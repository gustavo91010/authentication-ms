package com.ajudaqui.authenticationms.config;

import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.Users;
import java.util.List;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartUpRunner implements CommandLineRunner {

  @Autowired
  private UsersService usersServices;

  @Autowired
  private UsersAppDataService usersAppDataService;

  @Autowired
  private AuthService authService;

  @Autowired
  private ApplicationsService applicationService;

  @Override
  public void run(String... args) {

    Applications app = applicationService.getOrRegister("bill-manager");

    List<Users> all = usersServices.findAll();
    System.out.println("Quantos temos ma base? " + all.size());

    for (Users users : all) {
      try {

        if (!usersAppDataService.findByUsersEmail(users.getEmail()).isPresent()) {
          System.out.println("Tratando o email: " + users.getEmail());
          UsersRegister register = new UsersRegister();

          String name = users.getEmail().replaceAll("@.*", "");
          register.setName(name);
          register.setEmail(users.getEmail());
          register.setPassword("123456");
          register.setAplication(app.getName());
          authService.registerUser(register);
        }
        usersAppDataService.findByUsersEmail(users.getEmail())
            .ifPresent(u -> {

              u.setPassword(users.getPassword());
              usersAppDataService.save(u);
            });

      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    System.out.println("✅ Finalizou StartUpRunner.");

  }

}
