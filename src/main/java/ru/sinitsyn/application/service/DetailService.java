package ru.sinitsyn.application.service;

import java.util.*;

import ru.sinitsyn.domain.entities.car.details.Detail;
import ru.sinitsyn.application.service.repository.DetailRepository;
import ru.sinitsyn.domain.services.exceptions.DomainValidationException;

public final class DetailService {
    private final DetailRepository detailRepository;

    public DetailService(DetailRepository detailRepository){
        this.detailRepository = detailRepository;
    }

    public void CreateDetail(Detail detail) {
        if (detail == null) throw new DomainValidationException("detail must be not null");
        detailRepository.save(detail);
    }

    public Detail getDetail(UUID id){
        if (id == null) throw new DomainValidationException("Id can not be null");
        return detailRepository.getById(id);
    }

    public void updatePrice(UUID detailId, long newDetailPrice){
        if (detailId == null) throw new DomainValidationException("Id can not be null");
        Detail detail = detailRepository.getById(detailId);
        detail.updatePriceRub(newDetailPrice);

        detailRepository.save(detail);
    }

    public void addCompatibility(UUID detailId, UUID carId){
        Detail detail = detailRepository.getById(detailId);
        detail.addCompatibleCarId(carId);

        detailRepository.save(detail);
    }

    public List<Detail> listDetails() {
        return detailRepository.findAll();
    }


}