package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.OperationDao;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.OperationService;
import ru.sinitsyn.service.model.OperationModel;

import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {
    private final OperationDao operationDao;

    public OperationServiceImpl(OperationDao operationDao){
        this.operationDao = operationDao;
    }

    @Override
    public List<OperationModel> getAllOperations(OperationType type, Long accountId) {
        return operationDao.findAll(type, accountId)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }
}
