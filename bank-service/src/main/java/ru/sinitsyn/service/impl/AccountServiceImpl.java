package ru.sinitsyn.service.impl;

import ru.sinitsyn.dao.AccountDao;
import ru.sinitsyn.dao.OperationDao;
import ru.sinitsyn.dao.UserDao;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.List;

public class AccountServiceImpl implements AccountService{
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final OperationDao operationDao;

    public AccountServiceImpl(AccountDao accountDao, UserDao userDao, OperationDao operationDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.operationDao = operationDao;
    }

    @Override
    public Account createAccount(Long userId){
        User user = userDao.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Account account = new Account(BigDecimal.ZERO, user);
        return accountDao.save(account);
    }

    @Override
    public BigDecimal getBalance(Long accountId){
        Account account = accountDao.findById(accountId);
        if (account == null) throw new RuntimeException("Account not found");

        return account.getBalance();
    }

    @Override
    public void deposit(Long accountId, BigDecimal amount){
        Account account = accountDao.findById(accountId);
        if (account == null) {
            throw new RuntimeException("Account not found");
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
    public void withdraw(Long accountId, BigDecimal amount){
        Account account = accountDao.findById(accountId);
        if (account == null){
            throw new RuntimeException("Account not found");
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

    @Override
    public List<Operation> getOperations(Long accountId){
        Account account = accountDao.findById(accountId);
        if (account == null){
            throw new RuntimeException("Account not found");
        }
        return operationDao.findByAccountId(accountId);
    }
}
