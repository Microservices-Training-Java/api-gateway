package com.sub.authen.controller;

import com.sub.authen.facade.FacadeService;
import com.sub.authen.request.AuthRegistAccountRequest;
import com.sub.authen.request.AuthUserLoginRequest;
import com.sub.authen.response.AuthRegistAccountResponse;
import com.sub.authen.response.BaseResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final FacadeService authFacadeService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthUserLoginRequest request) {
        return ResponseEntity.ok(authFacadeService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<AuthRegistAccountResponse> register(@RequestBody AuthRegistAccountRequest request){
        return ResponseEntity.ok(authFacadeService.register(request));
    }
}
