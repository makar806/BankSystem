package ru.sinitsyn.app.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;

public record UserDTO(
    @Schema(description = "Идентификатор пользователя", example = "1")
    Long id,
    @Schema(description = "Логин пользователя", example = "Epstian")
    String login,
    @Schema(description = "Имя пользователя", example = "Иван")
    String name,
    @Schema(description = "Возрастт пользователя", example = "52")
    Integer age,
    @Schema(description = "Гендер пользователя", example = "MALE")
    Gender gender,
    @Schema(description = "Цвет волос пользователя", example = "BLONDE")
    HairColor hairColor
    ){}
