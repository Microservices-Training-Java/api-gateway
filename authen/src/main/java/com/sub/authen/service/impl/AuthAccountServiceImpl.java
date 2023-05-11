package com.sub.authen.service.impl;
import com.sub.authen.entity.AuthAccount;
import com.sub.authen.exception.BaseException;
import com.sub.authen.repository.projection.AccountUserProjection;
import com.sub.authen.repository.AuthAccountRepository;
import com.sub.authen.service.AuthAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthAccountServiceImpl implements AuthAccountService {
    @Autowired(required=true)
    private AuthAccountRepository repository;
    @Override
    public AuthAccount findByUserIdWithThrow(String userId) {
        return repository
                .findFirstByUserId(userId)
                .orElseThrow(
                        () -> {
                            throw new BaseException(404,"404","AccountNotFound");
                        });
    }

    @Override
    public AccountUserProjection findByUsername(String username) {
        return repository.find(username).orElseGet(() -> {
            throw new BaseException(404,"404","AccountNotFound");
        });
    }
    @Override
    public void enableLockPermanent(String email) {
        repository.enableLockPermanent(email);
    }
}
