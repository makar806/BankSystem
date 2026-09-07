package ru.sinitsyn.domain.entities.order;

import java.util.UUID;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

public final class ReadyCarOrder {
    private final UUID id;

    private final UUID customerId;
    private final UUID staffId;
    private final UUID carModelId;

    private final long priceAtCreation;

    private OrderStatus status;

    public ReadyCarOrder(UUID id, UUID carModelId, UUID staffId, UUID customerId, long priceAtCreation, OrderStatus status){
        this.id = DomainValidators.requireNonNull(id, "id");
        this.carModelId = DomainValidators.requireNonNull(carModelId, "carModelId");
        this.staffId = DomainValidators.requireNonNull(staffId, "staffId");
        this.customerId = DomainValidators.requireNonNull(customerId, "customerId");
        this.priceAtCreation = DomainValidators.requirePositive(priceAtCreation, "priceAtCreation");
        this.status = OrderStatus.CREATED;
    }

    public UUID getId() {return id;}
    public UUID getCustomerId() {return customerId;}
    public UUID getStaffId() {return staffId;}
    public UUID getCarModelId() {return carModelId;}
    public long getPriceAtCreation() {return priceAtCreation;}
    public OrderStatus getStatus() {return status;}

    private void requireStatus(OrderStatus expected){
        if (this.status != expected){
            throw new DomainValidationException("invalid status transition");
        }
    }

    public void approveByStaff() {
        requireStatus(OrderStatus.CREATED);
        this.status = OrderStatus.APPROVED_BY_STAFF;
    }

    public void readyForPickup() {
        requireStatus(OrderStatus.APPROVED_BY_STAFF);
        this.status = OrderStatus.READY_FOR_PICKUP;
    }

    public void completed() {
        requireStatus(OrderStatus.READY_FOR_PICKUP);
        this.status = OrderStatus.COMPLETED;
    }

    public void denied() {
        if (this.status == OrderStatus.COMPLETED){
            throw new DomainValidationException("Can not deny complete order");
        }
        this.status = OrderStatus.DENIED;
    }

}