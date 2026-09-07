package ru.sinitsyn.application;

import java.util.*;

import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.entities.car.CarConfiguration;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.entities.car.enums.PartType;
import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.domain.services.exceptions.EntityNotFoundException;
import ru.sinitsyn.domain.services.exceptions.IncompatibleComponentException;

public final class CarConfigurationBuilder {
    private final Car car;
    private final DetailRepository detailRepository;
    private final Map<PartType, UUID> selected;

    public CarConfigurationBuilder(Car car, DetailRepository detailRepository){
        if (car == null) throw new EntityNotFoundException("car must be not null");
        if (detailRepository == null) throw new EntityNotFoundException("Detail must be not null");

        this.car = car;
        this.selected = new EnumMap<>(PartType.class);
        this.detailRepository = detailRepository;
        this.selected.putAll(car.getBaseOption());
    }

    public CarConfigurationBuilder select(PartType type, UUID detailId){
        if (type == null) throw new EntityNotFoundException("type must be not null");
        if (detailId == null) throw new EntityNotFoundException("detail must be not null");

        Detail detail = detailRepository.getById(detailId);

        if (!detail.isCompatibleWith(car)){
            throw new IncompatibleComponentException("Detail" + detail.getName() + "is incompatible with this car");
        }

        selected.put(type, detailId);
        return this;
    }

    public CarConfiguration build() {
        return new CarConfiguration(Map.copyOf(selected));
    }
}