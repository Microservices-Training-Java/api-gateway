package com.sub.authen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountUserProjection {

  private String accountId;
  private String username;
  private String password;
  private String userId;
  private String email;
  private Boolean isActivated;
  private Boolean isLockPermanent;
  private Set<Role> roles;
}
