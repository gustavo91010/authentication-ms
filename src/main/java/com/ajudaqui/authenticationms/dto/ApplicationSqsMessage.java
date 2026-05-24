package com.ajudaqui.authenticationms.dto;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplicationSqsMessage {

  private String registerUrl;
  private String name;
  private String authorization;
  private Map<String, Object> payload;

  public JsonObject fromJson() {
    JsonObject sqsUsers = new JsonObject();

    sqsUsers.addProperty("register_url", this.registerUrl);
    sqsUsers.addProperty("authorization", this.authorization);
    sqsUsers.addProperty("name", this.name);

    sqsUsers.add("payload", new Gson().toJsonTree(payload));
    return sqsUsers;
  }

  public ApplicationSqsMessage(String registerUrl, String name, String authorization, Map<String, Object> payload) {
    this.registerUrl = registerUrl;
    this.name = name;
    this.authorization = authorization;
    this.payload = payload;
  }

}
