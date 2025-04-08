package com.ajudaqui.authenticationms.response;

public class AccessApi {
  private boolean isAccess;

  public AccessApi(boolean isAccess) {
    this.isAccess = isAccess;
  }

  public boolean isAccess() {
    return isAccess;
  }

  public void setAccess(boolean isAccess) {
    this.isAccess = isAccess;
  }

}
