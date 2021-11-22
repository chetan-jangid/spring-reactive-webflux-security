package com.security.reactivesecurity.router.api;

import com.security.reactivesecurity.config.security.AuthManager;
import com.security.reactivesecurity.http.TokenResponse;
import com.security.reactivesecurity.util.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiRouter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthManager authManager;

    public Mono<ServerResponse> hello() {
        return ServerResponse.ok().body(Mono.just("Hello!"), String.class);
    }

    public Mono<ServerResponse> world() {
        return ServerResponse.ok().body(Mono.just("World!"), String.class);
    }

    public Mono<ServerResponse> generateToken(ServerRequest request) {
        Optional<String> username = request.queryParam("username");
        Optional<String> password = request.queryParam("password");
        if (username.isPresent() && password.isPresent()) {
            Authentication authentication = authManager.toAuthentication(
                    username.get(), password.get(), List.of(
                            new SimpleGrantedAuthority(username.get().equals("user") ? "USER" : "ADMIN")
                    )
            );
            return ServerResponse.ok().body(
                    Mono.just(new TokenResponse(jwtTokenProvider.generateToken(authentication))), TokenResponse.class
            );
        }
        return Mono.error(new UsernameNotFoundException("Username & Password not provided."));
    }

}
