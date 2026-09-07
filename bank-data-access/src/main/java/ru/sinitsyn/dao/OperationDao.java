package ru.sinitsyn.dao;

import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;

import java.util.List;

public interface OperationDao {
    Operation save(Operation operation);
    List<Operation> findByAccountId(Long accountId);
    List<Operation> findAll(OperationType type, Long id);
}
