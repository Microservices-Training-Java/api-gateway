package com.sub.authen.filter;

import com.sub.authen.entity.Role;
import com.sub.authen.facade.FacadeService;
import com.sub.authen.service.AuthTokenService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
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

        if (Objects.nonNull(userId)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            var user = facadeService.findById(userId);
            var account = facadeService.findByUserIdWithThrow(user.getId());
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
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
            }
        }

        return chain.filter(exchange);
    }
}
