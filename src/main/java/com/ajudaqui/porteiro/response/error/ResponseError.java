package com.ajudaqui.porteiro.response.error;

public class ResponseError {
  private String message;

  public ResponseError(String message) {
    this.message = message;
  }

  public String response(String message) {
    return message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
