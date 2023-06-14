package com.sub.authen.entity;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuthAccount  {
    private String id;
    private String username;
    private String password;
    private Boolean isActivated = false;
    private Boolean isLockPermanent = false;
    private String userId;
    private Set<Role> roles;
}
