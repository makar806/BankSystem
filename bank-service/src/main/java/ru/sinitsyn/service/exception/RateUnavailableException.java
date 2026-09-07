package ru.sinitsyn.service.exception;

public class RateUnavailableException extends RuntimeException {
    public RateUnavailableException(String message) {
        super(message);
    }
    public RateUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
