package ru.sinitsyn.domain.entities.car.details.Wheels;

import ru.sinitsyn.domain.entities.car.details.Bodywork.BodyworkType;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Set;
import java.util.UUID;

public final class Wheels extends Detail {
    private final WheelsType wheelsType;
    private final long diametr;

    public Wheels(UUID id, String name, long priceRub, Set<UUID> compatibleCarIds, WheelsType wheelsType, long diametr){
        super(id, name, priceRub, compatibleCarIds);
        this.diametr = DomainValidators.requirePositive(diametr, "diametr");
        this.wheelsType = DomainValidators.requireNonNull(wheelsType, "wheelsType");
    }

    public WheelsType getWheelsType() {return wheelsType;}
    public long getDiametr() {return diametr;}
}
