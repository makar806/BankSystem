package ru.sinitsyn.dao;

import ru.sinitsyn.model.Operation;

import java.util.List;

public interface OperationDao {
    Operation save(Operation operation);
    List<Operation> findByAccountId(Long accountId);
}
