package com.example.configuration;

import com.sub.authen.config.EnableAuthentication;
import org.aibles.header.configuration.EnableCustomHeader;
import org.springframework.context.annotation.Configuration;

@EnableAuthentication
@Configuration
@EnableCustomHeader
public class GatewayConfiguration {
}
