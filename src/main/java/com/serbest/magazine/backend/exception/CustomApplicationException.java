package com.serbest.magazine.backend.exception;

import org.springframework.http.HttpStatus;

public class CustomApplicationException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public CustomApplicationException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public CustomApplicationException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
