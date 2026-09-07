package ru.sinitsyn.app.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.criteria.CriteriaBuilder;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;

public record CreateUserRequest (
        @Schema(description = "Логин пользователя, который будет отображаться")
        String login,
        @Schema(description = "Реальное имя пользователя")
                                 String name,
        @Schema(description = "Возраст пользователя")
                                 Integer age,
        @Schema(description = "Гендер пользователя")
                                 Gender gender,
        @Schema(description = "Цвет волос пользователя")
                                 HairColor hairColor) {}