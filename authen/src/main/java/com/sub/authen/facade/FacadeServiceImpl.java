package com.sub.authen.facade;

import com.example.BadRequestException;
import com.sub.authen.constant.URLConstant;
import com.sub.authen.entity.AuthAccount;
import com.sub.authen.entity.AuthUser;
import com.sub.authen.entity.AccountUserProjection;
import com.sub.authen.request.AuthRegistAccountRequest;
import com.sub.authen.response.AuthRegistAccountResponse;
import java.util.ArrayList;

import com.sub.authen.constant.CacheConstant;
import com.sub.authen.request.AuthUserLoginRequest;
import com.sub.authen.response.AuthActiveUserResponse;
import com.sub.authen.response.AuthInactiveUserResponse;
import com.sub.authen.response.AuthUserLoginResponse;
import com.sub.authen.service.*;
import com.sub.authen.utils.CryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacadeServiceImpl implements FacadeService{
//    @Autowired
//    private AuthAccountService authAccountService;
    @Autowired
    private LoginFailService loginFailService;
    @Autowired
    private AuthTokenService authTokenService;
    @Autowired
    private TokenRedisService tokenRedisService;
//    @Autowired
//    private AuthUserService authUserService;
    @Value("${application.authentication.access_token.life_time}")
    private Long accessTokenLifeTime;
    @Value("${application.authentication.refresh_token.life_time}")
    private Long refreshTokenLifeTime;
    @Autowired
    private RestTemplate restTemplate;
    public AuthUserLoginResponse login(AuthUserLoginRequest request) {
        var accountUser = findByUsername(request.getUsername());
        if (!accountUser.getIsActivated()) {
            return AuthInactiveUserResponse.from("Tài khoản chưa active");
        }

        loginFailService.checkLock(
                accountUser.getEmail(), accountUser.getUserId(), accountUser.getIsLockPermanent());

        if (!CryptUtil.getPasswordEncoder().matches(request.getPassword(), accountUser.getPassword())) {
            loginFailService.increaseFailAttempts(accountUser.getEmail());
            loginFailService.setLock(accountUser.getEmail());
            throw new BadRequestException("Username or Password is invalid");
        }
        loginFailService.resetFailAttempts(accountUser.getEmail());
        String accessToken =
                authTokenService.generateAccessToken(
                        accountUser.getUserId(), accountUser.getEmail(), accountUser.getUsername(), accountUser.getRoles());
        String refreshToken =
                authTokenService.generateRefreshToken(
                        accountUser.getUserId(), accountUser.getEmail(), accountUser.getUsername(), accountUser.getRoles());
        tokenRedisService.set(CacheConstant.CacheToken.KEY_CACHE_ACCESS_TOKEN, accountUser.getUserId(), accessToken);
        tokenRedisService.set(CacheConstant.CacheToken.KEY_CACHE_REFRESH_TOKEN, accountUser.getUserId(), refreshToken);
        authenticate(accountUser.getUsername(), accountUser.getUserId());
        return AuthActiveUserResponse.from(
                accessToken, refreshToken, accessTokenLifeTime, refreshTokenLifeTime);
    }
    @Override
    public void authenticate(String username, String userId) {
        var user = findById(userId);
        var usernamePasswordAuthToken =
            new UsernamePasswordAuthenticationToken(username, userId, new ArrayList<>());
        usernamePasswordAuthToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
    }

    @Override
    public AuthUser findById(String id) {
        return restTemplate.getForEntity(URLConstant.UserUrl+"/users/"+id,AuthUser.class).getBody();
    }

    @Override
    public AuthAccount findByUserIdWithThrow(String userId) {
        return restTemplate.getForEntity(URLConstant.UserUrl+"/account/"+userId,AuthAccount.class).getBody();
    }

    @Override
    public AccountUserProjection findByUsername(String username) {
        return restTemplate.getForEntity(URLConstant.UserUrl+"/accounts-by-username/?username="+username,AccountUserProjection.class).getBody();
    }

    @Override
    public void enableLockPermanent(String email) {
        restTemplate.getForEntity(URLConstant.UserUrl+"/accounts-lock/"+email,String.class);
    }

    @Override
    public AuthRegistAccountResponse register(AuthRegistAccountRequest request) {
        return restTemplate.getForEntity(URLConstant.UserUrl + "/");
    }
}
