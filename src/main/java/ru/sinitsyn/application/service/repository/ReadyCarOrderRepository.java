package ru.sinitsyn.application.service.repository;

import ru.sinitsyn.domain.entities.order.ReadyCarOrder;

import java.util.List;
import java.util.UUID;

public interface ReadyCarOrderRepository {
    void save(ReadyCarOrder order);
    ReadyCarOrder getById(UUID id);
    List<ReadyCarOrder> findAll();
}
