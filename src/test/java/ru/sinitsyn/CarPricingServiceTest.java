package ru.sinitsyn;

import org.junit.jupiter.api.Test;
import ru.sinitsyn.application.service.CarPricingService;
import ru.sinitsyn.application.service.repository.CarRepository;
import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.entities.car.CarConfiguration;
import ru.sinitsyn.domain.entities.car.details.Bodywork.Bodywork;
import ru.sinitsyn.domain.entities.car.details.Bodywork.BodyworkType;
import ru.sinitsyn.domain.entities.car.details.Engine.Engine;
import ru.sinitsyn.domain.entities.car.details.Engine.EngineType;
import ru.sinitsyn.domain.entities.car.details.Interier.InteriorDetail;
import ru.sinitsyn.domain.entities.car.details.Interier.UpholsteryType;
import ru.sinitsyn.domain.entities.car.details.Steering.Steering;
import ru.sinitsyn.domain.entities.car.details.Steering.SteeringMaterial;
import ru.sinitsyn.domain.entities.car.details.TransmissionBox.TransmissionBox;
import ru.sinitsyn.domain.entities.car.details.TransmissionBox.TransmissionBoxType;
import ru.sinitsyn.domain.entities.car.details.Wheels.Wheels;
import ru.sinitsyn.domain.entities.car.details.Wheels.WheelsType;
import ru.sinitsyn.domain.entities.car.enums.Color;
import ru.sinitsyn.domain.entities.car.enums.FuelType;
import ru.sinitsyn.domain.entities.car.enums.PartType;
import ru.sinitsyn.infrastructure.repository.InMemoryCarRepository;
import ru.sinitsyn.infrastructure.repository.InMemoryDetailRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CarPricingServiceTest {
    @Test
    void total_price_is_base_plus_sum_of_selected_details() {
        UUID carId = UUID.randomUUID();

        InMemoryCarRepository carRepo = new InMemoryCarRepository();
        InMemoryDetailRepository detailRepo = new InMemoryDetailRepository();

        UUID engineBaseId = UUID.randomUUID();
        UUID wheelsBaseId = UUID.randomUUID();
        UUID trans = UUID.randomUUID();
        UUID inter = UUID.randomUUID();
        UUID body = UUID.randomUUID();
        UUID steeringId = UUID.randomUUID();

        detailRepo.save(new Engine(engineBaseId, "engine", 150, 200000, Set.of(carId), EngineType.GAS));
        detailRepo.save(new Wheels(wheelsBaseId, "wheeeeee", 15011, Set.of(carId), WheelsType.SPORT, 17));
        detailRepo.save(new TransmissionBox(trans, "AT", 353432, Set.of(carId), TransmissionBoxType.AUTOMATIC));
        detailRepo.save(new InteriorDetail(UpholsteryType.FABRIC, "Inter", inter, 52525, Set.of(carId)));
        detailRepo.save(new Bodywork(body, "BABA", 191031, Set.of(carId), BodyworkType.SUV));
        detailRepo.save(new Steering(steeringId, "MAMAMIA", 424242, Set.of(carId), SteeringMaterial.PLASTIC));

        Map<PartType, UUID> selected = new EnumMap<>(PartType.class);
        selected.put(PartType.ENGINE, engineBaseId);
        selected.put(PartType.WHEELS, wheelsBaseId);
        selected.put(PartType.TRANSMISSION, trans);
        selected.put(PartType.INTERIOR, inter);
        selected.put(PartType.BODYWORK, body);
        selected.put(PartType.STEERING, steeringId);

        Car car = new Car(carId, "BMW", "320i", 1000000, FuelType.DIESEL, Color.BLACK, selected);
        carRepo.save(car);

        CarPricingService pricing = new CarPricingService(carRepo, detailRepo);
        long total = pricing.calculateTotalPrice(carId, new CarConfiguration(selected));

        assertEquals(200000 + 15011 + 353432 + 52525 + 191031 + 424242 + 1000000, total);


    }
}
