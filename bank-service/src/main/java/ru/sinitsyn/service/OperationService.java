package ru.sinitsyn.service;

import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.security.core.Authentication;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.model.OperationModel;

import java.math.BigDecimal;
import java.util.List;

public interface OperationService {
    List<OperationModel> getAllOperations(OperationType type, Long accountId);
    void deposit(Long accountId, BigDecimal amount, String authentication);
    void withdraw(Long accountId, BigDecimal amount, String authentication);
}
