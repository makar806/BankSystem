package ru.sinitsyn;

import ru.sinitsyn.application.service.*;
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
import ru.sinitsyn.infrastructure.repository.InMemoryCustomCarOrderRepository;
import ru.sinitsyn.infrastructure.repository.InMemoryDetailRepository;
import ru.sinitsyn.infrastructure.repository.InMemoryReadyCarOrderRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class App {

    public static void main(String[] args){
        InMemoryDetailRepository detailRepo = new InMemoryDetailRepository();
        InMemoryCarRepository carRepo = new InMemoryCarRepository();
        InMemoryReadyCarOrderRepository readyCarOrderRep = new InMemoryReadyCarOrderRepository();
        InMemoryCustomCarOrderRepository customCarOrderRepo = new InMemoryCustomCarOrderRepository();

        DetailService detailService = new DetailService(detailRepo);
        CarPricingService pricingService = new CarPricingService(carRepo, detailRepo);
        CarCatalogService carCatalogService = new CarCatalogService(carRepo, detailRepo);
        OrderService orderService = new OrderService(carRepo, detailRepo, customCarOrderRepo, readyCarOrderRep);

        UUID carModelId = UUID.randomUUID();
        UUID bodyId = UUID.randomUUID();
        UUID transmissionId = UUID.randomUUID();
        UUID wheelsId = UUID.randomUUID();
        UUID interiorId = UUID.randomUUID();
        UUID streeringId = UUID.randomUUID();
        UUID engineId = UUID.randomUUID();


        Bodywork body = new Bodywork(bodyId, "BODY SVO", 150000, Set.of(carModelId), BodyworkType.SUV);
        Engine engine = new Engine(engineId, "Diesil", 150, 300000, Set.of(carModelId), EngineType.GAS);
        TransmissionBox box = new TransmissionBox(transmissionId, "BAT", 200000, Set.of(carModelId), TransmissionBoxType.AUTOMATIC);
        Steering steering = new Steering(streeringId, "Stringi", 120000, Set.of(carModelId), SteeringMaterial.PLASTIC);
        InteriorDetail interior = new InteriorDetail(UpholsteryType.FABRIC, "Interior Fabric", interiorId, 80000, Set.of(carModelId));
        Wheels wheels = new Wheels(wheelsId, "Wheels 189i9q9e", 90000, Set.of(carModelId), WheelsType.SPORT, 18);

        detailService.CreateDetail(body);
        detailService.CreateDetail(engine);
        detailService.CreateDetail(box);
        detailService.CreateDetail(steering);
        detailService.CreateDetail(interior);
        detailService.CreateDetail(wheels);

        System.out.println("Total parts count: " + detailRepo.findAll().size());

        detailService.updatePrice(wheelsId, 66666);
        System.out.println("Wheels new price " + detailService.getDetail(wheelsId).getPriceRub());

        Map<PartType, UUID> base = new EnumMap<>(PartType.class);
        base.put(PartType.BODYWORK, bodyId);
        base.put(PartType.ENGINE, engineId);
        base.put(PartType.TRANSMISSION, transmissionId);
        base.put(PartType.STEERING, streeringId);
        base.put(PartType.INTERIOR, interiorId);
        base.put(PartType.WHEELS, wheelsId);

        Car carModel = new Car(carModelId, "BMW", "320i", 1000000, FuelType.DIESEL, Color.BLACK, base);

        carCatalogService.createCarModel(carModel);

        System.out.println("Car model " + carCatalogService.getCarModel(carModelId).getBrand() + " " + carCatalogService.getCarModel(carModelId).getModel());

        CarFilter filter = new CarFilter();
        filter.brand = "BMW";
        filter.engineType = EngineType.GAS;
        filter.transmissionBoxType = TransmissionBoxType.AUTOMATIC;

        var filtered = carCatalogService.ListCars(filter);

        System.out.println("Filtering:");
        for (Car c : filtered) {
            System.out.println("- " + c.getBrand() + " " + c.getModel() + " base = " + c.getBasePriceRub());
        }

        UUID engineUpgradeId = UUID.randomUUID();
        Engine engineUpgrade = new Engine(engineUpgradeId, "SUPER MEGA HAIP", 220, 4300000, Set.of(carModelId), EngineType.GAS);

        Map<PartType, UUID> overrides = new EnumMap<>(PartType.class);
        overrides.put(PartType.ENGINE, engineUpgradeId);

        CarConfiguration baseConfig = new CarConfiguration(carModel.getBaseOption());
        long baseTotal = pricingService.calculateTotalPrice(carModelId, baseConfig);

        System.out.println("Pricing");
        System.out.println("Total price: " + baseTotal);
    }
}
