package ru.sinitsyn.domain.entities.car;

import java.util.*;

import ru.sinitsyn.domain.entities.car.enums.PartType;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

public final class CarConfiguration {
    private final Map<PartType, UUID> selected;

    public CarConfiguration(Map<PartType, UUID> selected){
        if (selected == null || selected.isEmpty()){
            throw new DomainValidationException("selected options can not be empty");
        }
        EnumMap<PartType, UUID> copy = new EnumMap<>(PartType.class);
        copy.putAll(selected);

        for (PartType required : PartType.values()) {
            if (!copy.containsKey(required) || copy.get(required) == null){
                throw new DomainValidationException("missing componentos: " + required);
            }
        }

        this.selected = Collections.unmodifiableMap(copy);
    }

    public Map<PartType, UUID> getSelected(){
        return selected;
    }

    public UUID get(PartType type) {
        return selected.get(type);
    }

}