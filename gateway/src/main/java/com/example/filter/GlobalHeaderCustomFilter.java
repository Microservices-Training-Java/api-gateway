package com.example.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.header.constant.PayloadConstant;
import org.aibles.header.dto.Payload;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(1)
public class GlobalHeaderCustomFilter implements GlobalFilter {

  private final Payload payload;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("=== Start header custom filter ===");

      var language = exchange.getRequest().getHeaders().getFirst(PayloadConstant.LANGUAGE.getValue());

      payload.setUserId(exchange.getAttribute(PayloadConstant.USER_ID.getValue()));
      payload.setUsername(exchange.getAttribute(PayloadConstant.USERNAME.getValue()));
      payload.setLanguage(language);
      //TODO: get role from attribute and set for payload

      return chain.filter(exchange);

  }
}
