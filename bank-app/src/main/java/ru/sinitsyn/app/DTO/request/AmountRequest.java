package ru.sinitsyn.app.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record AmountRequest (@Schema(description = "Сумма денек") BigDecimal amount) {}
