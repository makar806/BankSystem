package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.dao.interfaces.UserDao;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.exception.InsufficientFundsException;
import ru.sinitsyn.service.model.AccountModel;
import ru.sinitsyn.service.model.OperationModel;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
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
    public AccountModel createAccount(Long userId){
        User user = userDao.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Account account = new Account(BigDecimal.ZERO, user);
        Account saveAccount = accountDao.save(account);
        return DomainMapper.toModel(saveAccount);
    }

    @Override
    public BigDecimal getBalance(Long accountId, String currentLogin){
        Account account = accountDao.findById(accountId);

        if (account == null) throw new RuntimeException("Account not found");

        if (!account.getOwner().getLogin().equals(currentLogin)) {
            throw new IllegalArgumentException("Access to this operation is prohibited");
        }

        return account.getBalance();
    }



    @Override
    public List<OperationModel> getOperations(Long accountId){
        Account account = accountDao.findById(accountId);
        if (account == null){
            throw new RuntimeException("Account not found");
        }
        return operationDao.findByAccountId(accountId)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountModel> getAccountsByUserId(Long id) {
        User user = userDao.findById(id);

        if (user == null) {
            throw new RuntimeException("user can not be empty");
        }

        return accountDao.findByOwnerId(id)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    public void deleteAccountById(Long id) {
        Account account = accountDao.findById(id);

        if (account == null) {
            throw new RuntimeException("no account with this ID was found");
        }

        operationDao.deleteByAccountId(account.getId());

        accountDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountModel> getAllAccounts() {
        return accountDao.findAll()
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }
}
