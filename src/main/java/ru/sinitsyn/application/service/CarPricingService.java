package ru.sinitsyn.application.service;

import java.util.UUID;

import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.application.service.repository.CarRepository;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.entities.car.CarConfiguration;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

public final class CarPricingService {
    private final CarRepository carRepository;
    private final DetailRepository detailRepository;

    public CarPricingService(CarRepository carRepository, DetailRepository detailRepository){
        this.carRepository = carRepository;
        this.detailRepository = detailRepository;
    }

    public long calculateTotalPrice(UUID carId, CarConfiguration configuration){
        if (configuration == null) {
            throw new DomainValidationException("configuration must be not null");
        }
        Car car = carRepository.getById(carId);

        long total = car.getBasePriceRub();
        for (UUID detailId: configuration.getSelected().values()){
            Detail detail = detailRepository.getById(detailId);
            total += detail.getPriceRub();
        }
        return total;
    }

}