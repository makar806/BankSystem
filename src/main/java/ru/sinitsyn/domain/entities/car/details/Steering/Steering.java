package ru.sinitsyn.domain.entities.car.details.Steering;

import ru.sinitsyn.domain.entities.car.details.Bodywork.BodyworkType;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Set;
import java.util.UUID;

public final class Steering extends Detail {
    private final SteeringMaterial steeringMaterial;

    public Steering(UUID id, String name, long priceRub, Set<UUID> compatibleCarIds, SteeringMaterial steeringMaterial){
        super(id, name, priceRub, compatibleCarIds);
        this.steeringMaterial = DomainValidators.requireNonNull(steeringMaterial, "steeringMaterial");

    }

    public SteeringMaterial getSteeringMaterial() {return steeringMaterial;}
}
