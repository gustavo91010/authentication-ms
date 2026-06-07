package com.ajudaqui.porteiro.config.dbmigrations;

import java.time.LocalDateTime;
import com.ajudaqui.porteiro.entity.Applications;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

@ChangeUnit(id = "create-default-application", order = "003", author = "gustavo")
public class CreateDefaultApplicationMigration {

  private final MongoTemplate mongoTemplate;

  public CreateDefaultApplicationMigration(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Execution
  public void execution() {

    if (mongoTemplate.exists(
        Query.query(Criteria.where("name").is("porteiro_api")),
        Applications.class)) {
      return;
    }

    Applications application = new Applications();
    application.setId("6a1ba9823a434a40267d4042");
    application.setName("porteiro_api");
    application.setRegisterUrl("http://localhost:8082");
    application.setSecretId("GmL3M1KJFVi7JSHxtrxDKKSNRjjw0NBWHJ3uys6qlxENlCZURgNNxDsN//yGY2die+CnV7MmgeqgzfPKFoo9Gg==");
    application.setCreatedAt(LocalDateTime.now());
    mongoTemplate.save(application);
  }

  @RollbackExecution
  public void rollback() {
  }

}
