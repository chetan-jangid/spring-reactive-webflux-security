package com.security.reactivesecurity.util.token;

import org.springframework.security.core.Authentication;

public abstract class TokenProvider {

    public abstract String generateToken(Authentication authentication);
    public abstract Authentication getAuthentication(String token) throws Exception;
    public abstract void validateToken(String token) throws Exception;

}
