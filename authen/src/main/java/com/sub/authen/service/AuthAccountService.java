package com.sub.authen.service;


import com.sub.authen.entity.AuthAccount;
import com.sub.authen.repository.projection.AccountUserProjection;

public interface AuthAccountService {
    AuthAccount findByUserIdWithThrow(String userId);
    AccountUserProjection findByUsername(String userId);
    void enableLockPermanent(String email);
}
