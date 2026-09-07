package ru.sinitsyn.domain.entities.user;

import java.time.LocalDate;
import java.util.UUID;

import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

public final class Customer extends User{
    private final LocalDate birthDate;
    private final String phone;
    private final String mail;

    public Customer(LocalDate birthDate, String phone, String mail, UUID id, String name) {
        super(id, name);
        this.birthDate = DomainValidators.requireNonNull(birthDate, "birthDate");
        this.phone = DomainValidators.requireNonBlank(phone, "phone");
        this.mail = DomainValidators.requireNonBlank(mail, "mail");
    }

    public LocalDate getBirthDate() {return birthDate;}
    public String getMail() {return mail;}
    public String getPhone() {return phone;}
}