package com.sub.authen.filter;

import com.sub.authen.facade.FacadeService;
import com.sub.authen.service.AuthTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
@Slf4j
@Order(0)
public class CustomHeaderFilter implements WebFilter {

    private final AuthTokenService authTokenService;
    private final FacadeService facadeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("Start filter");

        var modifiedHeaders = new HttpHeaders();
        modifiedHeaders.add("userId", exchange.getAttribute("userId"));
        modifiedHeaders.add("userRoles", exchange.getAttribute("userRoles"));
        modifiedHeaders.add("username", exchange.getAttribute("username"));
        var request = exchange.getRequest().mutate()
            .headers(httpHeaders -> httpHeaders.addAll(modifiedHeaders))
            .build();

        return chain.filter(exchange.mutate().request(request).build());
    }
}

