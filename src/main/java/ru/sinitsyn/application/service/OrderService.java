package ru.sinitsyn.application.service;

import ru.sinitsyn.application.CarConfigurationBuilder;
import ru.sinitsyn.application.service.repository.CarRepository;
import ru.sinitsyn.application.service.repository.CustomCarOrderRepository;
import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.application.service.repository.ReadyCarOrderRepository;
import ru.sinitsyn.domain.entities.car.Car;
import ru.sinitsyn.domain.entities.car.CarConfiguration;
import ru.sinitsyn.domain.entities.car.enums.PartType;
import ru.sinitsyn.domain.entities.order.CustomCarOrder;
import ru.sinitsyn.domain.entities.order.OrderStatus;
import ru.sinitsyn.domain.entities.order.ReadyCarOrder;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class OrderService {
    private final CarRepository carRepository;
    private final DetailRepository detailRepository;
    private final CustomCarOrderRepository customCarOrderRepo;
    private final ReadyCarOrderRepository readyCarOrderRepo;

    private final CarPricingService pricingService;

    public OrderService(CarRepository carRepository, DetailRepository detailRepository, CustomCarOrderRepository customCarOrderRepo, ReadyCarOrderRepository readyCarOrderRepo){
        this.carRepository = carRepository;
        this.detailRepository = detailRepository;
        this.customCarOrderRepo = customCarOrderRepo;
        this.readyCarOrderRepo = readyCarOrderRepo;

        this.pricingService = new CarPricingService(carRepository, detailRepository);
    }

    public ReadyCarOrder createReadyOrder(UUID customerId, UUID staffId, UUID carModelId){
        if (customerId == null) throw new DomainValidationException("customer must be not null");
        if (staffId == null) throw new DomainValidationException("staff must be not null");
        if (carModelId == null) throw new DomainValidationException("car must be not null");

        Car car = carRepository.getById(carModelId);

        CarConfiguration baseConfig = new CarConfiguration(car.getBaseOption());
        long priceAtCreation = pricingService.calculateTotalPrice(carModelId, baseConfig);
        ReadyCarOrder order = new ReadyCarOrder(UUID.randomUUID(), carModelId, staffId, customerId, priceAtCreation, OrderStatus.CREATED);

        readyCarOrderRepo.save(order);
        return order;
    }

    public List<ReadyCarOrder> ListReadyOrders(UUID staffId, UUID customerId){
        return readyCarOrderRepo.findAll().stream()
                .filter(o -> staffId == null || o.getStaffId().equals(staffId))
                .filter(o -> customerId == null || o.getCustomerId().equals(customerId))
                .toList();
    }

    public CustomCarOrder createCustomOrder(UUID customerId, UUID staffId, UUID carModelId, Map<PartType, UUID> overrides){
        if (customerId == null) throw new DomainValidationException("customerId must be not null");
        if (staffId == null) throw new DomainValidationException("staffId must be not null");
        if (carModelId == null) throw new DomainValidationException("carModelId must be not null");

        Car car = carRepository.getById(carModelId);

        CarConfigurationBuilder builder = new CarConfigurationBuilder(car, detailRepository);
        if (overrides != null) {
            for (Map.Entry<PartType, UUID> e : overrides.entrySet()){
                if (e.getKey() == null || e.getValue() == null) {
                    throw new DomainValidationException("overrides cant contains null");
                }
                builder.select(e.getKey(), e.getValue());
            }
        }

        CarConfiguration config = builder.build();
        long priceAtCreation = pricingService.calculateTotalPrice(carModelId, config);

        CustomCarOrder order = new CustomCarOrder(UUID.randomUUID(), customerId, staffId, carModelId, config.getSelected(), priceAtCreation);

        customCarOrderRepo.save(order);
        return order;
    }

    public List<CustomCarOrder> listCustomOrders(UUID staffId, UUID customerId){
        return customCarOrderRepo.findAll().stream()
                .filter(o -> staffId == null || o.getStuffId().equals(staffId))
                .filter(o -> customerId == null || o.getCustomerId().equals(customerId))
                .toList();
    }
}
