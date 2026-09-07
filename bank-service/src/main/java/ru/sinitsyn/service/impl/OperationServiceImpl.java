package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.OperationService;
import ru.sinitsyn.service.exception.InsufficientFundsException;
import ru.sinitsyn.service.model.OperationModel;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OperationServiceImpl implements OperationService {
    private final OperationDao operationDao;
    private final AccountDao accountDao;

    public OperationServiceImpl(OperationDao operationDao, AccountDao accountDao){
        this.operationDao = operationDao;
        this.accountDao = accountDao;
    }

    @Override
    public List<OperationModel> getAllOperations(OperationType type, Long accountId) {
        return operationDao.findAll(type, accountId)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount, String currentLogin){
        Account account = accountDao.findById(accountId);
        if (!account.getOwner().getLogin().equals(currentLogin)){
            throw new IllegalArgumentException("Access to this operation is prohibited");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Amount must be greater then zero");
        }

        account.setBalance(account.getBalance().add(amount));
        accountDao.save(account);

        Operation operation = new Operation(amount, OperationType.DEPOSIT, account);
        operationDao.save(operation);
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount, String currentLogin){
        Account account = accountDao.findById(accountId);
        if (!account.getOwner().getLogin().equals(currentLogin)){
            throw new IllegalArgumentException("Access to this operation is prohibited");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Amount must be greater then zero");
        }

        if (account.getBalance().compareTo(amount) < 0){
            throw new InsufficientFundsException("Insufficient");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountDao.save(account);

        Operation operation = new Operation(amount, OperationType.WITHDRAW, account);
        operationDao.save(operation);
    }
}
