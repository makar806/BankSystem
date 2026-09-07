package ru.sinitsyn.service;

import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.model.OperationModel;

import java.util.List;

public interface OperationService {
    List<OperationModel> getAllOperations(OperationType type, Long accountId);
}
