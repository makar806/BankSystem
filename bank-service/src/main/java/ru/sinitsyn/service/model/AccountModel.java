package ru.sinitsyn.service.model;

import java.math.BigDecimal;

public record AccountModel (
    Long id,
    BigDecimal balance,
    Long ownerId
) {}
