package ru.sinitsyn.rates.DTO;

import java.math.BigDecimal;
import java.time.Instant;

public record RateMessage(
    String currency,
    BigDecimal rateToRub,
    Instant timestamp
    ) {}
