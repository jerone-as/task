package com.mykare.task.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException {

    public HttpStatus status;

    public GlobalException(String message, HttpStatus httpStatus) {
        super(message);
        status = httpStatus;
    }

    public static class UnAuthorized extends RuntimeException {
        public UnAuthorized(String message) {
            super(message);
        }
    }

    public static class Forbidden extends RuntimeException {
        public Forbidden(String message) {
            super(message);
        }
    }
    
    public static class InternalServerError extends RuntimeException {
        public InternalServerError(String message) {
            super(message);
        }
    }
}
