package ru.sinitsyn.app.DTO.request;

import java.math.BigDecimal;

public record TransferRequest (Long fromAccountId, Long toAccountId, BigDecimal amount) {}