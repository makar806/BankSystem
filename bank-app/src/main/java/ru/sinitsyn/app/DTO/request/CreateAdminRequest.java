package ru.sinitsyn.app.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateAdminRequest {

    @Schema(description = "Логин админа")
    String login;

    @Schema(description = "Пароль Админа")
    String password;
}
