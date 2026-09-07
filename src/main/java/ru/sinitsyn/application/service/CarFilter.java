package ru.sinitsyn.application.service;

import ru.sinitsyn.domain.entities.car.details.Bodywork.BodyworkType;
import ru.sinitsyn.domain.entities.car.details.Engine.EngineType;
import ru.sinitsyn.domain.entities.car.details.TransmissionBox.TransmissionBoxType;
import ru.sinitsyn.domain.entities.car.enums.Color;

public final class CarFilter {
    public Long minBasePrice;
    public Long maxBasePrice;


    public String brand;

    public BodyworkType bodyworkType;

    public EngineType engineType;
    public Long maxPower;
    public Long minPower;

    public TransmissionBoxType transmissionBoxType;

    public Color interierColor;
}