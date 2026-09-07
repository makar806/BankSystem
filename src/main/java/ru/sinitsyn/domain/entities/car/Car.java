package ru.sinitsyn.domain.entities.car;

import java.util.*;

import ru.sinitsyn.domain.entities.car.enums.*;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

public class Car {
    private final UUID id;
    private final String brand;
    private final String model;

    private final long basePriceRub;

    private final FuelType fuelType;
    private final Color color;

    private final Map<PartType, UUID> baseOption;

    public Car(UUID id, String brand, String model, long basePriceRub, FuelType fuelType, Color color, Map<PartType, UUID> baseOption){
        this.id = Objects.requireNonNull(id, "id");
        this.brand = DomainValidators.requireNonBlank(brand, "brand");
        this.model = DomainValidators.requireNonBlank(model, "model");

        if (basePriceRub <= 0){
            throw new DomainValidationException("base price must be positive");
        }
        this.basePriceRub = basePriceRub;
        this.fuelType = Objects.requireNonNull(fuelType, "fuelType");
        this.color = Objects.requireNonNull(color, "color");

        if (baseOption == null || baseOption.isEmpty()) {
            throw new DomainValidationException("baseoptions cant not be empty");
        }

        EnumMap<PartType, UUID> copy = new EnumMap<>(PartType.class);
        copy.putAll(baseOption);

        for (PartType required : PartType.values()){
            if (!copy.containsKey(required)){
                throw new DomainValidationException("base option missing componento: " + required);
            }
        }

        for (Map.Entry<PartType, UUID> e : copy.entrySet()) {
            PartType key = e.getKey();
            UUID opt = e.getValue();
            if (opt == null){
                throw new DomainValidationException("baseOption has null option for: " + key);
            }
        }
        this.baseOption = Collections.unmodifiableMap(copy);

    }
    public UUID getId() {return id;}
    public String getBrand() {return brand;}
    public String getModel() {return model;}
    public long getBasePriceRub() {return basePriceRub;}

    public FuelType getFuelType() {return fuelType;}
    public Color getColor() {return color;}

    public Map<PartType, UUID> getBaseOption() {return baseOption;}
    public UUID getBaseOption(PartType type) {return baseOption.get(type);}

}