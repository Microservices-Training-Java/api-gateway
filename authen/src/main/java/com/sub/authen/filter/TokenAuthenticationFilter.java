package com.sub.authen.filter;

import com.sub.authen.constant.ClaimsConstant;
import com.sub.authen.facade.FacadeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sub.authen.service.AuthTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenService authTokenService;
    private final FacadeService facadeService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info(
                "(doFilterInternal)request: {}, response: {}, filterChain: {}",
                request,
                response,
                filterChain);
        final String accessToken = request.getHeader("Authorization");

        if (Objects.isNull(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!accessToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwtToken = accessToken.substring(7);
        Claims claims = authTokenService.getClaimsFromAccessToken(jwtToken);
        String username = claims.get(ClaimsConstant.USERNAME.getValue(), String.class);


        String userId;
        try {

            userId = authTokenService.getSubjectFromAccessToken(jwtToken);
            request.setAttribute(ClaimsConstant.USER_ID.getValue(), userId);
            request.setAttribute(ClaimsConstant.USERNAME.getValue(), username);
            //TODO: get role and set Attribute

        } catch (Exception ex) {
            log.error("(doFilterInternal)get subject token failed");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("(doFilterInternal)userId : {}", userId);
        if (Objects.nonNull(userId)
                && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            var user = facadeService.findById(userId);
            var account = facadeService.findByUserIdWithThrow(user.getId());
            if (authTokenService.validateAccessToken(jwtToken, userId)) {
                var usernamePasswordAuthToken =
                        new UsernamePasswordAuthenticationToken(
                                account.getUsername(), user.getId(), new ArrayList<>());
                usernamePasswordAuthToken.setDetails(user);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

