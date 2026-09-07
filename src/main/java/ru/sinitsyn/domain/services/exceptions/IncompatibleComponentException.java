package ru.sinitsyn.domain.services.exceptions;

public class IncompatibleComponentException extends RuntimeException{
    public IncompatibleComponentException(String message){
        super(message);
    }
}