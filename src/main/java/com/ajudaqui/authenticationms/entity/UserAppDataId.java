package com.ajudaqui.authenticationms.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserAppDataId implements Serializable {
  private Long userId;
  private Long appId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof UserAppDataId))
      return false;
    UserAppDataId that = (UserAppDataId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(appId, that.appId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, appId);
  }
}
