package com.sub.authen.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AuthRegistAccountResponse extends BaseResponse{
  @NonNull
  private String username;
  @NonNull
  private String password;
}
