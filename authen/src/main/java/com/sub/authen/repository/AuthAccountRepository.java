package com.sub.authen.repository;

import com.sub.authen.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthAccountRepository extends JpaRepository<AuthAccount, String> {
 Optional<AuthAccount> findFirstByUserId(String id);
 Optional<AuthAccount> findByUsername(String username);
 @Query(
         "select new com.sub.authen.repository.AccountUserProjection("
                 + "a.id, "
                 + "a.username, "
                 + "a.password, "
                 + "a.userId, "
                 + "u.email, "
                 + "a.isActivated,"
                 + "a.isLockPermanent)"
                 + "from AuthAccount a inner join AuthUser u on a.userId = u.id where a.username = :username")
 Optional<AccountUserProjection> find(String username);
 @Transactional
 @Modifying
 @Query(nativeQuery = true, value = "UPDATE account SET is_lock_permanent = true FROM accounts a INNER JOIN user_okrs u ON a.user_id = u.id WHERE u.email = :email")
 void enableLockPermanent(@Param("email") String email);
}