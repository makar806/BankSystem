package ru.sinitsyn.service.model;

import java.math.BigDecimal;
import java.time.Instant;

public record ConvertedBalanceModel(
    Long accountId,
    BigDecimal balanceRub,
    String currency,
    BigDecimal rateToRub,
    BigDecimal convertedBalance,
    Instant rateTimestamp
    ){}
