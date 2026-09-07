package ru.sinitsyn.application.service;

import ru.sinitsyn.application.service.repository.CarRepository;
import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.entities.car.details.Bodywork.Bodywork;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.entities.car.details.Engine.Engine;
import ru.sinitsyn.domain.entities.car.details.TransmissionBox.TransmissionBox;
import ru.sinitsyn.domain.entities.car.enums.PartType;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public final class CarCatalogService {

    private final CarRepository carRepository;
    private final DetailRepository detailRepository;

    public CarCatalogService(CarRepository carRepository, DetailRepository detailRepository){
        this.carRepository = carRepository;
        this.detailRepository = detailRepository;
    }

    public void createCarModel(Car car) {
        if (car == null) throw new DomainValidationException("car must be not null");

        for (Map.Entry<PartType, UUID> e : car.getBaseOption().entrySet()) {
            UUID detailId = e.getValue();
            Detail detail = detailRepository.getById(detailId);

            detail.addCompatibleCarId(car.getId());
            detailRepository.save(detail);
        }

        carRepository.save(car);
    }

    public Car getCarModel(UUID carId) {
        if (carId == null ) throw new DomainValidationException("carId must be not null");

        return carRepository.getById(carId);
    }

    public List<Car> ListCars(CarFilter filter){
        List<Car> all = carRepository.findAll();
        if (filter == null) return all;

        return all.stream()
                .filter(c -> filter.maxBasePrice == null || c.getBasePriceRub() >= filter.minBasePrice)
                .filter(c -> filter.minBasePrice == null || c.getBasePriceRub() >= filter.minBasePrice)
                .filter(c -> filter.brand == null || c.getBrand().equals(filter.brand))
                .filter(c -> filter.interierColor == null || c.getColor().equals(filter.interierColor))

                .filter(c -> matchesBodywork(c, filter))
                .filter(c -> matchesEngine(c, filter))
                .filter(c -> matchesTransmission(c, filter))


                .toList();
    }

    public boolean matchesBodywork(Car car, CarFilter filter) {
        if (filter.bodyworkType == null) return true;

        UUID bodyId = car.getBaseOption(PartType.BODYWORK);
        Detail detail = detailRepository.getById(bodyId);

        if (!(detail instanceof Bodywork bodywork)){
            return false;
        }

        return bodywork.getBodyworkType() == filter.bodyworkType;
    }

    public boolean matchesEngine(Car car, CarFilter filter) {
        boolean needType = filter.engineType != null;
        boolean needMinPower = filter.minPower != null;
        boolean needMaxPower = filter.maxPower != null;

        if (!needMaxPower && !needMinPower && !needType) return true;

        UUID engineId = car.getBaseOption(PartType.ENGINE);
        Detail detail = detailRepository.getById(engineId);

        if (!(detail instanceof Engine engine)){
            return false;
        }

        if (needType && engine.getEnginType() != filter.engineType) return false;
        if (needMaxPower && engine.getPower() < filter.maxPower) return false;
        if (needMinPower && engine.getPower() > filter.minPower) return false;

        return true;
    }

    public boolean matchesTransmission(Car car, CarFilter filter) {
        if (filter.transmissionBoxType == null) return true;

        UUID trId = car.getBaseOption(PartType.TRANSMISSION);
        Detail detail = detailRepository.getById(trId);

        if (!(detail instanceof TransmissionBox transmissionBox)) {
            return false;
        }

        return transmissionBox.getTransmissionBoxType() == filter.transmissionBoxType;

    }
}