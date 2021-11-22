package com.security.reactivesecurity.config.router;

import com.security.reactivesecurity.router.api.ApiRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class ApiRouterConfiguration {

    private final ApiRouter apiRouter;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return route(GET("/hello"), request -> apiRouter.hello())
                .andRoute(GET("/world"), request -> apiRouter.world())
                .andRoute(GET("/generate"), apiRouter::generateToken);
    }

}
