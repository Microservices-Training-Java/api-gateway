package com.sub.authen.config;

import com.sub.authen.filter.TokenAuthenticationFilterWebFlux;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class WebFluxSecurityConfig {
  private final TokenAuthenticationFilterWebFlux tokenAuthenticationFilter;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
    return http
        .csrf().disable()
        .addFilterBefore(tokenAuthenticationFilter, SecurityWebFiltersOrder.FIRST)
        .build();
  }
}
