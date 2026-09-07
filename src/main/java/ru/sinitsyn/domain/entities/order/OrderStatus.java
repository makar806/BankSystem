package ru.sinitsyn.domain.entities.order;

public enum OrderStatus {
    CREATED,
    APPROVED_BY_STAFF,
    READY_FOR_PICKUP,
    COMPLETED,
    DENIED
}