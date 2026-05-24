package com.ajudaqui.authenticationms.entity;

import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Roles {

  private Long id;

  private ERoles name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ERoles getName() {
    return name;
  }

  public void setName(ERoles name) {
    this.name = name;
  }

}
