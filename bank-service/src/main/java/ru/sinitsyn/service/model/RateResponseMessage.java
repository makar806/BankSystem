package ru.sinitsyn.service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record RateResponseMessage (
        String currency,
        BigDecimal rateToRub,
        Instant timestamp,
        String error
) {}

