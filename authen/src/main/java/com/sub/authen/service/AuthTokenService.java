package com.sub.authen.service;

import io.jsonwebtoken.Claims;
import com.sub.authen.entity.Role;

import java.util.Set;

public interface AuthTokenService {
    String getSubjectFromAccessToken(String accessToken);
    boolean validateAccessToken(String accessToken, String userId);
    Claims getClaimsFromAccessToken(String token);
    String generateAccessToken(String userId, String email, String username, Set<Role> roles);
    String generateRefreshToken(String userId, String email, String username, Set<Role> roles);
}
