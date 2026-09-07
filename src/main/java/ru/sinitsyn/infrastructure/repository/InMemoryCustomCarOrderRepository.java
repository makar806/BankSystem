package ru.sinitsyn.infrastructure.repository;

import ru.sinitsyn.application.service.repository.CustomCarOrderRepository;
import ru.sinitsyn.domain.entities.order.CustomCarOrder;
import ru.sinitsyn.domain.entities.order.ReadyCarOrder;
import ru.sinitsyn.domain.services.exceptions.EntityNotFoundException;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class InMemoryCustomCarOrderRepository implements CustomCarOrderRepository {
    private final List<CustomCarOrder> orders = new ArrayList<>();

    public void save(CustomCarOrder order) {
        for (int i = orders.size() - 1; i >= 0; i--){
            CustomCarOrder o = orders.get(i);
            if (o.getId().equals(order.getId())){
                orders.remove(i);
            }
        }
        orders.add(order);
    }

    public CustomCarOrder getById(UUID id){
        for (CustomCarOrder o : orders){
            if (o.getId().equals(id)) return o;
        }
        throw new EntityNotFoundException("Custom car order not found");
    }

    public List<CustomCarOrder> findAll() {
        return new ArrayList<>(orders);
    }

}
