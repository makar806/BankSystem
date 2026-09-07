package ru.sinitsyn.app.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record AccountDTO(
        @Schema(description = "Идентификатор аккаунта", example = "555")
        Long id,
        @Schema(description = "Текущий баланс аккаунта", example = "100000")
        BigDecimal balance,
        @Schema(description = "id владельца аккаунта")
        Long ownerId
) {}
