package ru.sinitsyn.app.DTO;

import java.math.BigDecimal;
import java.time.Instant;

public record ConvertedBalanceDTO (
    Long accountId,
    BigDecimal balanceRub,
    String currency,
    BigDecimal rateToRub,
    BigDecimal convertedBalance,
    Instant rateTimestamp
){}
