package com.ajudaqui.authenticationms.dto;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ApplicationSqsMessage {

  private String registerUrl;
  private String name;
  private String authorization;
  private Map<String, Object> payload;

  public JsonObject fromJson() {
    JsonObject sqsUsers = new JsonObject();

    sqsUsers.addProperty("url", this.registerUrl);
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

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(String registerUrl) {
    this.registerUrl = registerUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthorization() {
    return authorization;
  }

  public void setAuthorization(String authorization) {
    this.authorization = authorization;
  }

  public Map<String, Object> getPayload() {
    return payload;
  }

  public void setPayload(Map<String, Object> payload) {
    this.payload = payload;
  }

}
