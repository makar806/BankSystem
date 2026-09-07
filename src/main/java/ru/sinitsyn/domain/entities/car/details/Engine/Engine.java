package ru.sinitsyn.domain.entities.car.details.Engine;

import java.util.*;

import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;


public final class Engine extends Detail {
    private final long power;
    private final EngineType enginType;

    public Engine(UUID id, String name, long power, long priceRub, Set<UUID> compatibleCarIds, EngineType enginType){
        super(id, name, priceRub, compatibleCarIds);
        this.enginType = DomainValidators.requireNonNull(enginType, "enginType");
        this.power = DomainValidators.requireNonNegative(power, "power");
    }

    public long getPower() {return power;}
    public EngineType getEnginType() {return enginType;}
}