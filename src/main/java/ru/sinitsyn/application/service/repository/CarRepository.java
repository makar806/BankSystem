package ru.sinitsyn.application.service.repository;

import java.util.*;
import ru.sinitsyn.domain.entities.car.Car;

public interface CarRepository {
    void save(Car car);
    Car getById(UUID id);
    List<Car> findAll();
}
