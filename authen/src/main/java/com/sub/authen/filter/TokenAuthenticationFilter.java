package com.sub.authen.filter;

import com.sub.authen.entity.AuthUser;
import com.sub.authen.entity.Role;
import com.sub.authen.facade.FacadeService;
import com.sub.authen.service.AuthTokenService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
@Order(-1)
public class TokenAuthenticationFilter implements WebFilter {

    private final AuthTokenService authTokenService;
    private final FacadeService facadeService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("(filter)exchange: {}, chain: {}", exchange, chain);

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        final String accessToken = request.getHeaders().getFirst("Authorization");

        if (Objects.isNull(accessToken)) {
            return chain.filter(exchange);
        }

        if (!accessToken.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        var jwtToken = accessToken.substring(7);
        String userId;
        try {
            userId = authTokenService.getSubjectFromAccessToken(jwtToken);
        } catch (Exception ex) {
            return chain.filter(exchange);
        }

        if (Objects.nonNull(userId)) {
            var user = facadeService.findById(userId);
            var account = facadeService.findByUserIdWithThrow(user.getId());
            exchange.getAttributes().put("userId", user.getId());
            exchange.getAttributes().put("userRoles", account.getRoles());
            exchange.getAttributes().put("username", account.getUsername());
            if (authTokenService.validateAccessToken(jwtToken, userId)) {
                Set<Role> roles = account.getRoles();
                // Convert the Set<Role> to a collection of GrantedAuthority
                Collection<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
                var usernamePasswordAuthToken =
                    new UsernamePasswordAuthenticationToken(
                        account.getUsername(), user.getId(), authorities);
                usernamePasswordAuthToken.setDetails(user);

                return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(usernamePasswordAuthToken));
            }
        }
        return chain.filter(exchange);
    }

}