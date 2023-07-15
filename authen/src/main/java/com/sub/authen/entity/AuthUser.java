package com.sub.authen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class AuthUser {
    @Id
    private String id;
    private String name;
    private String phone;
    private String email;
    private Integer dateOfBirth;
    private String gender;
    private String address;
    private String avatar;
    public static AuthUser from(String name, String email) {
        var user = new AuthUser();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
