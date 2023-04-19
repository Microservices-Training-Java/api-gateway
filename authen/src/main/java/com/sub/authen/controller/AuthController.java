package com.sub.authen.controller;

import com.sub.authen.facade.FacadeService;
import com.sub.authen.request.AuthUserLoginRequest;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(
             @RequestBody AuthUserLoginRequest request) {
        try {
            return ResponseEntity.ok(authFacadeService.login(request));

        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
