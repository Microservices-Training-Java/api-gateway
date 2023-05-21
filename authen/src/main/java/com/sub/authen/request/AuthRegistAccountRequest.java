package com.sub.authen.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthRegistAccountRequest {
  private String username;
  private String password;
  private String confirmPassword;
  private String email;
}