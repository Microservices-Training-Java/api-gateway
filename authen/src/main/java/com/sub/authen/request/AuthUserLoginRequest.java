package com.sub.authen.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthUserLoginRequest {

    @NotBlank
    private String username;
    @NotBlank private String password;
}
