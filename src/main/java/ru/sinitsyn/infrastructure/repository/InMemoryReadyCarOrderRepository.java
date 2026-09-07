package ru.sinitsyn.infrastructure.repository;

import ru.sinitsyn.application.service.repository.ReadyCarOrderRepository;
import ru.sinitsyn.domain.entities.order.ReadyCarOrder;
import ru.sinitsyn.domain.services.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class InMemoryReadyCarOrderRepository implements ReadyCarOrderRepository {
    private final List<ReadyCarOrder> orders = new ArrayList<>();

    public void save(ReadyCarOrder order) {
        for (int i = orders.size() - 1; i >= 0; i--){
            ReadyCarOrder o = orders.get(i);
            if (o.getId().equals(order.getId())){
                orders.remove(i);
            }
        }
        orders.add(order);
    }

    public ReadyCarOrder getById(UUID id) {
        for (ReadyCarOrder o : orders) {
            if (o.getId().equals(id)) return o;
        }
        throw new EntityNotFoundException("Ready cr order not found");
    }

    public List<ReadyCarOrder> findAll() {
        return new ArrayList<>(orders);
    }

}