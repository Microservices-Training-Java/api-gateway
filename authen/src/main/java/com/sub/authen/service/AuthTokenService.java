package com.sub.authen.service;

import io.jsonwebtoken.Claims;

public interface AuthTokenService {
    String getSubjectFromAccessToken(String accessToken);
    boolean validateAccessToken(String accessToken, String userId);
    String generateAccessToken(String userId, String email, String username);
    String generateRefreshToken(String userId, String email, String username);
    Claims getClaimsFromAccessToken(String token);
}
