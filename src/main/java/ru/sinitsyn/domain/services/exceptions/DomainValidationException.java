package ru.sinitsyn.domain.services.exceptions;

public class DomainValidationException extends RuntimeException{

    public DomainValidationException(String massage){
        super(massage);
    }

    public DomainValidationException(String massege, Throwable cause){
        super(massege, cause);
    }
}