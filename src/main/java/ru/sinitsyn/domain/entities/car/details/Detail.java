package ru.sinitsyn.domain.entities.car.details;

import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class Detail {

    private final UUID id;
    private final String name;
    private long priceRub;
    private final Set<UUID> compatibleCarIds;

    protected Detail(UUID id, String name, long priceRub, Set<UUID> compatibleCarIds){

        this.id = DomainValidators.requireNonNull(id, "id");

        this.name = DomainValidators.requireNonBlank(name, "name");

        this.priceRub = DomainValidators.requireNonNegative(priceRub, "price");

        if (compatibleCarIds == null || compatibleCarIds.isEmpty()) {
            throw new DomainValidationException("compatibleCarIds must be not empty");
        }
        this.compatibleCarIds = new HashSet<>(compatibleCarIds);
    }

    public UUID getId() {return id;};
    public String getName() {return name;};
    public long getPriceRub() {return priceRub;};
    public Set<UUID> getCompatibleCarIds() {return Collections.unmodifiableSet(compatibleCarIds);};

    public boolean isCompatibleWith(Car car) {
        return compatibleCarIds.contains(car.getId());
    }

    public void updatePriceRub(long newPriceRub){
        this.priceRub = newPriceRub;
    }

    public void addCompatibleCarId(UUID carModelId){
        DomainValidators.requireNonNull(carModelId, "carModelId");
        compatibleCarIds.add(carModelId);
    }

}