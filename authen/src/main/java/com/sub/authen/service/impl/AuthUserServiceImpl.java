package com.sub.authen.service.impl;
import com.sub.authen.entity.AuthUser;
import com.sub.authen.exception.BaseException;
import com.sub.authen.repository.AuthUserRepository;
import com.sub.authen.service.AuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthUserServiceImpl implements AuthUserService {
    @Autowired
    private AuthUserRepository repository;
    @Override
    public AuthUser findById(String id) {
        if (log.isDebugEnabled()) log.debug("(findById)id: {}", id);
        return repository.findById(id).orElseGet(() -> {
            log.error("(findById)id: {} not found", id);
            throw new BaseException(404,"404","UserNotFound");
        });
    }

}
