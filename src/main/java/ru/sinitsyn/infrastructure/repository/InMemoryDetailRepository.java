package ru.sinitsyn.infrastructure.repository;

import java.util.*;

import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.EntityNotFoundException;

public final class InMemoryDetailRepository implements DetailRepository{
    private final List<Detail> details = new ArrayList<>();

    public void save(Detail detail){
        for (int i = details.size() - 1; i >= 0; i--){
            Detail d = details.get(i);
            if (d.getId().equals(detail.getId())){
                details.remove(i);
            }
        }
        details.add(detail);
    }

    public Detail getById(UUID id){
        for (Detail detail : details){
            if (detail.getId().equals(id)){
                return detail;
            }
        }
        throw new EntityNotFoundException("Detail not found" + id);
    }

    public List<Detail> findAll(){
        return new ArrayList<>(details);
    }
}