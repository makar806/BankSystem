package ru.sinitsyn.application.service.repository;

import java.util.*;
import ru.sinitsyn.domain.entities.car.details.Detail;

public interface DetailRepository {
    void save(Detail detail);
    Detail getById(UUID id);
    List<Detail> findAll();
}
