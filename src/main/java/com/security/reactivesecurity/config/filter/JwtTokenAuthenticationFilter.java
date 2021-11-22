package com.security.reactivesecurity.config.filter;

import com.security.reactivesecurity.util.token.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public record JwtTokenAuthenticationFilter(JwtTokenProvider tokenProvider) implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String token = resolveToken(exchange.getRequest());
        if (StringUtils.hasText(token)) {
            try {
                tokenProvider.validateToken(token);
                return chain.filter(exchange).contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(tokenProvider.getAuthentication(token))
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {
        final String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = "Bearer ";
        if (StringUtils.hasText(token) && token.startsWith(headerPrefix)) {
            return token.substring(7);
        }
        return null;
    }

}
