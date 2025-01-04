package com.example.ttcn2etest.exception;

public class JwtTokenInvalid extends RuntimeException {
    public JwtTokenInvalid(String message) {
        super(message);
    }
}
