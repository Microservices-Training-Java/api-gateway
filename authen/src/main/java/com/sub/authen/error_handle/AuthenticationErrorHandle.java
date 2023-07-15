package com.sub.authen.error_handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sub.authen.utils.DateUtils;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationErrorHandle implements ServerAuthenticationEntryPoint {

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
    //TODO: khi gặp bất cứ bug nào trong quá trình filter nó sẽ nhảy vào đây
    // ví dụ như endpoint cần có role nhưng request không mang role
    log.info("enter into authentication error handle");
    var error = new HashMap<String, Object>();
    error.put("status", HttpStatus.UNAUTHORIZED.value());
    error.put("timestamp", DateUtils.getCurrentDateTimeStr());
    error.put("message", "Unauthenticated.");

    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    try {
      return response.writeWith(Mono.just(response.bufferFactory()
          .wrap(new ObjectMapper().writeValueAsBytes(error))));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
