package com.sub.authen.constant;

public enum  ClaimsConstant {
  USER_ID("userId"),
  USER_ROLES("userRoles"),
  USERNAME("username");

  private final String value;

  private ClaimsConstant(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
