package com.sub.authen.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({GatewayAuthenConfig.class,
        RedisConfig.class,
        RestTemplateConfig.class,
        WebFluxSecurityConfig.class,
        JpaAuthTransactionConfiguration.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAuthentication {

}
