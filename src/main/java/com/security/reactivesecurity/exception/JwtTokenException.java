package com.security.reactivesecurity.exception;

public class JwtTokenException extends Exception {

    public JwtTokenException(String message) {
        super(message);
    }

    public static String invalidJwtToken() {
        return "Invalid JWT!";
    }

    public static String tokenExpired() {
        return "Token expired!";
    }

}
