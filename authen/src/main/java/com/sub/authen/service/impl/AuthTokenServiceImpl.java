package com.sub.authen.service.impl;

import com.sub.authen.entity.Role;
import com.sub.authen.service.AuthTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthTokenServiceImpl implements AuthTokenService {
    @Value("${application.authentication.access_token.jwt_secret:xxx}")
    private String accessTokenJwtSecret;
    @Value("${application.authentication.access_token.life_time}")
    private Long accessTokenLifeTime;
    @Value("${application.authentication.refresh_token.jwt_secret:xxx}")
    private String refreshTokenJwtSecret;
    @Value("${application.authentication.refresh_token.life_time}")
    private Long refreshTokenLifeTime;
    @Override
    public String getSubjectFromAccessToken(String accessToken) {
        log.debug("(getSubjectFromAccessToken)accessToken: {}", accessToken);
        return getClaim(accessToken, Claims::getSubject, accessTokenJwtSecret);
    }
    private <T> T getClaim(String token, Function<Claims, T> claimsResolve, String secretKey) {
        return claimsResolve.apply(getClaims(token, secretKey));
    }
    private Claims getClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Claims getClaimsFromAccessToken(String token) {
        return getClaims(token, accessTokenJwtSecret);
    }

    @Override
    public boolean validateAccessToken(String accessToken, String userId) {
        if(getSubjectFromAccessToken(accessToken).equals(userId) == false || isExpiredToken(accessToken,accessTokenJwtSecret) == true){
            return false;
        }
        return true;
    }
    private boolean isExpiredToken(String token, String secretKey) {
        return getClaim(token, Claims::getExpiration, secretKey).before(new Date());
    }
    @Override
    public String generateAccessToken(String userId, String email, String username, Set<Role> roles) {
        var claims = new HashMap<String, Object>();
        claims.put("email", email);
        claims.put("username", username);
        claims.put("roles", roles);
        return generateToken(userId, claims, accessTokenLifeTime, accessTokenJwtSecret);
    }
    private String generateToken(
        String subject, Map<String, Object> claims, long tokenLifeTime, String jwtSecret) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
    }
    @Override
    public String generateRefreshToken(String userId, String email, String username, Set<Role> roles) {
        log.info("(generateRefreshToken)userId: {}, email: {}, username: {}", userId, email, username);
        var claims = new HashMap<String, Object>();
        claims.put("email", email);
        claims.put("username", username);
        claims.put("roles", roles);
        return generateToken(userId, claims, refreshTokenLifeTime, refreshTokenJwtSecret);
    }

}

