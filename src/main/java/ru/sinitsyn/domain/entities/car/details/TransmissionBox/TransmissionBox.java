package ru.sinitsyn.domain.entities.car.details.TransmissionBox;

import ru.sinitsyn.domain.entities.car.details.Bodywork.BodyworkType;
import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.domain.services.exceptions.validation.DomainValidators;

import java.util.Set;
import java.util.UUID;

public final class TransmissionBox extends Detail {
    private final TransmissionBoxType transmissionBoxType;

    public TransmissionBox(UUID id, String name, long priceRub, Set<UUID> compatibleCarIds, TransmissionBoxType transmissionBoxType){
        super(id, name, priceRub, compatibleCarIds);
        this.transmissionBoxType = DomainValidators.requireNonNull(transmissionBoxType, "transmissionBoxType");
    }

    public TransmissionBoxType getTransmissionBoxType() {return transmissionBoxType;}
}
