package com.security.reactivesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ReactiveSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSecurityApplication.class, args);
	}

}
