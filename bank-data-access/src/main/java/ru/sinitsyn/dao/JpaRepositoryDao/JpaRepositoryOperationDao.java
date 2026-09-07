package ru.sinitsyn.dao.JpaRepositoryDao;

import org.springframework.stereotype.Repository;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.dao.repositories.OperationRepository;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;

import java.util.List;

@Repository
public class JpaRepositoryOperationDao implements OperationDao{
    public OperationRepository operationRepository;

    public JpaRepositoryOperationDao(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }


    @Override
    public Operation save(Operation operation){
        return operationRepository.save(operation);
    }

    @Override
    public List<Operation> findByAccountId(Long accountId){
        return operationRepository.findByAccountId(accountId);
    }

    @Override
    public List<Operation> findAll(OperationType operationType, Long id) {return operationRepository.findAll(operationType, id);}

    @Override
    public void deleteByAccountId(Long id) {
        operationRepository.deleteByAccountId(id);
    }

}
