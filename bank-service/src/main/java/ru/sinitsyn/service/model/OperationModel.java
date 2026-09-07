package ru.sinitsyn.service.model;

import ru.sinitsyn.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OperationModel (
        Long id,
        BigDecimal amount,
        OperationType type,
        LocalDateTime createdAt,
        Long accountId
) {}
