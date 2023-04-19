package com.sub.authen.service;


import com.sub.authen.entity.AuthUser;

public interface AuthUserService {
    AuthUser findById(String id);
}
