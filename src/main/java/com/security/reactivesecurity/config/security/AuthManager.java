package com.security.reactivesecurity.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthManager {

    public Authentication toAuthentication(String username, String password, Collection<GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

}
