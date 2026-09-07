package ru.sinitsyn.service.model;

import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;

import java.util.Set;

public record UserModel (
        Long id,
        String login,
        String name,
        Integer age,
        Gender gender,
        HairColor hairColor,
        Set<Long> friendId
) {}
