package com.example.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aibles.header.util.PayloadUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(2)
public class GlobalHeaderForwarderFilter implements GlobalFilter {

  private final PayloadUtil payloadUtil;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    log.info("=== Start header forwarder filter ===");

    var request = exchange.getRequest().mutate()
        .headers(httpHeaders -> httpHeaders.addAll(payloadUtil.fromHeader())).build();
    return chain.filter(exchange.mutate().request(request).build());
  }
}
