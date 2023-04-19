package com.sub.authen.entity;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Data
@NoArgsConstructor
@Table(name = "accounts")
public class AuthAccount  {
    @Id
    private String id;


    @PrePersist
    public void ensureId() {
        this.id = Objects.isNull(this.id) ? UUID.randomUUID().toString() : this.id;
    }

    private String username;
    private String password;
    private Boolean isActivated = false;
    private Boolean isLockPermanent = false;
    private String userId;

}
