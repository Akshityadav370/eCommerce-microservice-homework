package com.api_gateway.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalLoggingFilter {
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//   pre-filter
        log.info("Logging from Global Pre: {}", exchange.getRequest().getURI());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            log.info("Logging from Global Post: {}", exchange.getResponse().getStatusCode());
        }));
    }
}
