package com.ajudaqui.authenticationms.config.dbmigrations;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

@ChangeUnit(id = "initial-setup", order = "001", author = "admin")
public class InitialSetupMigration {

  @Execution
  public void execution(MongoTemplate mongoTemplate) {

    Roles roleMod = createRoleIfNotFound(mongoTemplate, ERoles.ROLE_MODERATOR);
    Roles user_role = createRoleIfNotFound(mongoTemplate, ERoles.ROLE_USER);
    Roles roleAdmin = createRoleIfNotFound(mongoTemplate, ERoles.ROLE_ADMIN);

    if (!mongoTemplate.exists(Query.query(Criteria.where("email").is("admin@ajudaqui.com")), Users.class)) {

      UsersAppData adminAppData = UsersAppData.builder()
          .appId("6a1ba9823a434a40267d4042")
          .appName("porteiro_api")
          .password("$2a$10$G9urthVYdGrXupVF1.Pq2u7AFTl7V.nBLUzZI6ysrhMaIXsm6Foay")
          // .password("$2a$10$vL4PXOamKa7B5jFNBvvbVOeP3t.Lm94mktrJyJg5inlcYs1ogcr3K")
          .isActive(true)
          .accessToken(UUID.fromString("77a1e032-823a-4793-ac96-2e34567b3059")) // ID mestre do properties
          .roles(new HashSet<>(Arrays.asList(roleMod, roleAdmin, user_role)))
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .build();

      Users admin = Users.builder()
          .name("Admin")
          .email("admin@ajudaqui.com")
          .createdAt(LocalDateTime.now())
          .updatedAt(LocalDateTime.now())
          .usersAppData(new HashSet<>(Arrays.asList(adminAppData)))
          .build();

      mongoTemplate.save(admin);
    }
  }

  @RollbackExecution
  public void rollbackExecution(MongoTemplate mongoTemplate) {
    // Rollback opcional
  }

  private Roles createRoleIfNotFound(MongoTemplate mongoTemplate, ERoles roleName) {
    Query query = new Query(Criteria.where("name").is(roleName));
    Roles role = mongoTemplate.findOne(query, Roles.class);
    if (role == null) {
      role = new Roles();
      role.setName(roleName);
      role = mongoTemplate.save(role);
    }
    return role;
  }
}
