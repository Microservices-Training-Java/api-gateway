package com.example.configuration;

import com.sub.authen.config.EnableAuthentication;
import org.aibles.header.configuration.EnableCustomHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableAuthentication
@Configuration
public class GatewayConfiguration {
}
