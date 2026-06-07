package com.ajudaqui.porteiro.entity;

import com.ajudaqui.porteiro.utils.enuns.ERoles;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "roles")
public class Roles {

  @Id
  private String id;

  private ERoles name;

}
