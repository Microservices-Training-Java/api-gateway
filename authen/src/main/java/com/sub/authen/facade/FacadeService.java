package com.sub.authen.facade;

import com.sub.authen.request.AuthUserLoginRequest;
import com.sub.authen.response.AuthUserLoginResponse;

public interface FacadeService {
    AuthUserLoginResponse login(AuthUserLoginRequest request);
    void authenticate(String username, String userId);

}
