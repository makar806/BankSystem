package ru.sinitsyn.domain.entities.user;

import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.*;

public final class Employee extends User {
    private final StaffRoles role;

    public Employee(StaffRoles role, UUID id, String name){
        super(id, name);
        this.role = DomainValidators.requireNonNull(role, "role");
    }

    public StaffRoles getRole() {return role;}
    
}