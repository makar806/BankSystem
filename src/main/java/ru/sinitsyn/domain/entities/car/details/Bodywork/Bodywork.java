package ru.sinitsyn.domain.entities.car.details.Bodywork;

import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Set;
import java.util.UUID;


public final class Bodywork extends Detail {
    private final BodyworkType bodyworkType;

    public Bodywork(UUID id, String name, long priceRub, Set<UUID> compatibleCarIds, BodyworkType bodyworkType){
        super(id, name, priceRub, compatibleCarIds);
        this.bodyworkType = DomainValidators.requireNonNull(bodyworkType, "bodyworkType");
    }

    public BodyworkType getBodyworkType() {return bodyworkType;}

}
