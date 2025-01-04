package com.example.ttcn2etest.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    private String errorMessage;

    public UsernameAlreadyExistsException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

