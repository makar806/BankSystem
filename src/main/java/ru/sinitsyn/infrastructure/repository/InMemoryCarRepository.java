package ru.sinitsyn.infrastructure.repository;

import java.util.*;

import ru.sinitsyn.application.service.repository.CarRepository;
import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.services.exceptions.EntityNotFoundException;

public final class InMemoryCarRepository implements CarRepository{
    private final List<Car> cars = new ArrayList<>();

    public void save(Car car){
        for (int i = cars.size() - 1; i >= 0; i--){
            Car c = cars.get(i);
            if (c.getId().equals(car.getId())){
                cars.remove(i);
            }
        }
        cars.add(car);
    }

    public Car getById(UUID id){
        for (Car car : cars){
            if (car.getId().equals(id)){
                return car;
            }
        }
        throw new EntityNotFoundException("Car not found" + id);
    }

    public List<Car> findAll(){
        return new ArrayList<>(cars);
    }
}