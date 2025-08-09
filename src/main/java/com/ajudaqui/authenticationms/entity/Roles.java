package com.ajudaqui.authenticationms.entity;

import javax.persistence.*;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Entity
public class Roles {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
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
