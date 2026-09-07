package ru.sinitsyn.domain.entities.order;

public enum CustomOrderStatus {
    CREATED,
    APPROVE_BY_STAFF,
    READY_FOR_PICKUP,
    COMPLETED,
    CONFIG_ERROR,
    DENIED
}