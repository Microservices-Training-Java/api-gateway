package com.sub.authen.facade;

import com.sub.authen.entity.AuthAccount;
import com.sub.authen.entity.AuthUser;
import com.sub.authen.entity.AccountUserProjection;
import com.sub.authen.request.AuthUserLoginRequest;
import com.sub.authen.response.AuthUserLoginResponse;

public interface FacadeService {
    AuthUserLoginResponse login(AuthUserLoginRequest request);
    void authenticate(String username, String userId);
    AuthUser findById(String id);
    AuthAccount findByUserIdWithThrow(String userId);
    AccountUserProjection findByUsername(String userId);
    void enableLockPermanent(String email);

}
