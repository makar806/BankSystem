package ru.sinitsyn;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.hamcrest.collection.IsIn;
import ru.sinitsyn.application.CarConfigurationBuilder;
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
import ru.sinitsyn.domain.services.exceptions.IncompatibleComponentException;
import ru.sinitsyn.infrastructure.repository.InMemoryCarRepository;
import ru.sinitsyn.infrastructure.repository.InMemoryDetailRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CarConfigurationBuilderTest {

    @Test
    void builder_starts_from_base_and_replace_selected() {
        UUID carId = UUID.randomUUID();

        InMemoryDetailRepository detailRepo = new InMemoryDetailRepository();

        UUID engineBaseId = UUID.randomUUID();
        UUID wheelsBaseId = UUID.randomUUID();
        UUID trans = UUID.randomUUID();
        UUID inter = UUID.randomUUID();
        UUID body = UUID.randomUUID();
        UUID steeringId = UUID.randomUUID();

        detailRepo.save(new Engine(UUID.randomUUID(), "engine", 150, 200000, Set.of(carId), EngineType.GAS));
        detailRepo.save(new Wheels(UUID.randomUUID(), "wheeeeee", 150, Set.of(carId), WheelsType.SPORT, 17));
        detailRepo.save(new TransmissionBox(trans, "AT", 120000, Set.of(carId), TransmissionBoxType.AUTOMATIC));
        detailRepo.save(new InteriorDetail(UpholsteryType.FABRIC, "Inter", inter, 52525, Set.of(carId)));
        detailRepo.save(new Bodywork(body, "BABA", 123123, Set.of(carId), BodyworkType.SUV));
        detailRepo.save(new Steering(steeringId, "MAMAMIA", 424242, Set.of(carId), SteeringMaterial.PLASTIC));

        Map<PartType, UUID> base = new EnumMap<>(PartType.class);
        base.put(PartType.ENGINE, engineBaseId);
        base.put(PartType.WHEELS, wheelsBaseId);
        base.put(PartType.TRANSMISSION, trans);
        base.put(PartType.INTERIOR, inter);
        base.put(PartType.BODYWORK, body);
        base.put(PartType.STEERING, steeringId);

        Car car = new Car(carId, "BMW", "320i", 1000000, FuelType.DIESEL, Color.BLACK, base);

        UUID engineNewId = UUID.randomUUID();
        detailRepo.save(new Engine(engineNewId, "NewEngine", 150, 300000, Set.of(carId), EngineType.GAS));

        CarConfiguration c = new CarConfigurationBuilder(car, detailRepo)
                .select(PartType.ENGINE, engineNewId)
                .build();

        assertEquals(engineNewId, c.get(PartType.ENGINE));
        assertEquals(wheelsBaseId, c.get(PartType.WHEELS));
    }

    @Test
    void builder_throws_on_incompatible_detail() {
        UUID carId = UUID.randomUUID();
        UUID otherCarId = UUID.randomUUID();

        InMemoryDetailRepository detailRepo = new InMemoryDetailRepository();

        UUID engineBaseId = UUID.randomUUID();
        UUID wheelsBaseId = UUID.randomUUID();
        UUID trans = UUID.randomUUID();
        UUID inter = UUID.randomUUID();
        UUID body = UUID.randomUUID();
        UUID steeringId = UUID.randomUUID();

        detailRepo.save(new Engine(UUID.randomUUID(), "engine", 150, 200000, Set.of(carId), EngineType.GAS));
        detailRepo.save(new Wheels(UUID.randomUUID(), "wheeeeee", 150, Set.of(carId), WheelsType.SPORT, 17));
        detailRepo.save(new TransmissionBox(trans, "AT", 120000, Set.of(carId), TransmissionBoxType.AUTOMATIC));
        detailRepo.save(new InteriorDetail(UpholsteryType.FABRIC, "Inter", inter, 52525, Set.of(carId)));
        detailRepo.save(new Bodywork(body, "BABA", 123123, Set.of(carId), BodyworkType.SUV));
        detailRepo.save(new Steering(steeringId, "MAMAMIA", 424242, Set.of(carId), SteeringMaterial.PLASTIC));

        Map<PartType, UUID> base = new EnumMap<>(PartType.class);
        base.put(PartType.ENGINE, engineBaseId);
        base.put(PartType.WHEELS, wheelsBaseId);
        base.put(PartType.TRANSMISSION, trans);
        base.put(PartType.INTERIOR, inter);
        base.put(PartType.BODYWORK, body);
        base.put(PartType.STEERING, steeringId);

        Car car = new Car(carId, "BMW", "320i", 1000000, FuelType.DIESEL, Color.BLACK, base);

        UUID engineBadId = UUID.randomUUID();
        detailRepo.save(new Engine(engineBadId, "badengine", 50, 2000000, Set.of(otherCarId), EngineType.GAS));

        assertThrows(IncompatibleComponentException.class, () -> new CarConfigurationBuilder(car, detailRepo).select(PartType.ENGINE, engineBadId));
    }

}
