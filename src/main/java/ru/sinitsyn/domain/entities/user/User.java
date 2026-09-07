package ru.sinitsyn.domain.entities.user;

import java.util.*;
import java.util.Date;

import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

public abstract class User{
    private final UUID id;
    private final String name;

    protected User(UUID id, String name){
        this.id = DomainValidators.requireNonNull(id, "id");
        this.name = DomainValidators.requireNonBlank(name, "name");
    }

    public UUID getId() {return id;}
    public String getName() {return name;}

}