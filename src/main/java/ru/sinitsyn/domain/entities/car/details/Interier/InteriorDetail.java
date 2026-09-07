package ru.sinitsyn.domain.entities.car.details.Interier;

import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Set;
import java.util.UUID;

public final class InteriorDetail extends Detail {
    private final UpholsteryType upholsteryType;

    public InteriorDetail(UpholsteryType upholsteryType, String name, UUID id, long priceRub, Set<UUID> compatibleCarIds){
        super(id, name, priceRub, compatibleCarIds);
        this.upholsteryType = DomainValidators.requireNonNull(upholsteryType, "upholsteryType");
    }

    public UpholsteryType getUpholsteryType() {return upholsteryType;}
}

