package ru.sinitsyn.app.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.sinitsyn.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OperationDTO (
        @Schema(description = "Идентификатор операции")
        Long id,
        @Schema(description = "Сумма операции")
        BigDecimal amount,
        @Schema(description = "Тип операции")
        OperationType type,
        @Schema(description = "Id аккаунта, на котором происходит операция")
        Long accountId,
        @Schema(description = "Время создания операции")
        LocalDateTime createdAt
){
}
