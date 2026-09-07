package ru.sinitsyn.domain.entities.order;

import java.util.*;

import ru.sinitsyn.domain.services.exceptions.DomainValidationException;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;
import ru.sinitsyn.domain.entities.car.enums.PartType;

public final class CustomCarOrder {
    private final UUID id;
    private final UUID customerId;
    private final UUID stuffId;
    private final UUID carModelId;

    private final Map<PartType, UUID> selectedComponentOptionIds;

    private CustomOrderStatus status;
    private final long priceAtCreation;

    public CustomCarOrder(UUID id, UUID customerId, UUID stuffId, UUID carModelId, Map<PartType, UUID> selectedComponentOptionIds, long priceAtCreation){
        this.id = DomainValidators.requireNonNull(id, "id");
        this.customerId = DomainValidators.requireNonNull(customerId, "customerId");
        this.stuffId = DomainValidators.requireNonNull(stuffId, "stuffId");
        this.carModelId = DomainValidators.requireNonNull(carModelId, "carModelId");
        this.priceAtCreation = DomainValidators.requirePositive(priceAtCreation, "priceAtCreation");

        EnumMap<PartType, UUID> copy = new EnumMap<>(PartType.class);
        copy.putAll(selectedComponentOptionIds);

        for (Map.Entry<PartType, UUID> e : copy.entrySet()){
            if (e.getKey() == null || e.getValue() == null) {
                throw new DomainValidationException("NO"); // CHANGE IT
            }
        }
        this.selectedComponentOptionIds = Collections.unmodifiableMap(copy);
        this.status = CustomOrderStatus.CREATED;
    }

    public UUID getId() {return id;}
    public UUID getCustomerId() {return customerId;}
    public UUID getStuffId() {return stuffId;}
    public UUID getCarModelId() {return carModelId;}
    public long getPriceAtCreation() {return priceAtCreation;}
    public Map<PartType, UUID> getSelectedComponentOptionIds() {return selectedComponentOptionIds;}


    private void requireStatus(CustomOrderStatus expected){
        if (this.status != expected){
            throw new DomainValidationException("invalid status transition");
        }
    }

    public void approveByStaff() {
        requireStatus(CustomOrderStatus.CREATED);
        this.status = CustomOrderStatus.APPROVE_BY_STAFF;
    }

    public void readyForPickup() {
        requireStatus(CustomOrderStatus.APPROVE_BY_STAFF);
        this.status = CustomOrderStatus.READY_FOR_PICKUP;
    }

    public void completed() {
        requireStatus(CustomOrderStatus.READY_FOR_PICKUP);
        this.status = CustomOrderStatus.COMPLETED;
    }

    public void denied() {
        if (this.status == CustomOrderStatus.CONFIG_ERROR){
            throw new DomainValidationException("Can not deny complete order");
        }
        this.status = CustomOrderStatus.DENIED;
    }
}

