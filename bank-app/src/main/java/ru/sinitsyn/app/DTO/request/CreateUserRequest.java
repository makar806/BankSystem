package ru.sinitsyn.app.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;

import java.time.LocalDate;

public record CreateUserRequest (
        @Schema(description = "Логин пользователя, который будет отображаться")
        String login,
        @Schema(description = "Реальное имя пользователя")
                                 String name,
        @Schema(description = "Дата рождения")
        LocalDate birthDate,
        @Schema(description = "Гендер пользователя")
                                 Gender gender,
        @Schema(description = "Цвет волос пользователя")
                                 HairColor hairColor,
        @Schema(description = "Пароль пользователя")
                                 String password) {}
