package ru.sinitsyn.domain.services.exceptions;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String massage){
        super(massage);
    }
}