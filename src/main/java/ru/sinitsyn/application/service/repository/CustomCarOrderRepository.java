package ru.sinitsyn.application.service.repository;

import ru.sinitsyn.domain.entities.order.CustomCarOrder;
import ru.sinitsyn.domain.entities.order.ReadyCarOrder;

import java.util.List;
import java.util.UUID;

public interface CustomCarOrderRepository {
    void save(CustomCarOrder order);
    CustomCarOrder getById(UUID id);
    List<CustomCarOrder> findAll();
}

