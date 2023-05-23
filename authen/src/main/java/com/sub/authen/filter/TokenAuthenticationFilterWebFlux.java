package com.sub.authen.filter;

import com.sub.authen.facade.FacadeService;
import com.sub.authen.service.AuthTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationFilterWebFlux implements WebFilter {

    private final AuthTokenService authTokenService;
    private final FacadeService facadeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Start filter");
        return null;
    }
}

