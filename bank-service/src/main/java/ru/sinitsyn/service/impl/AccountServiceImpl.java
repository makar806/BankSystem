package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.AccountDao;
import ru.sinitsyn.dao.OperationDao;
import ru.sinitsyn.dao.UserDao;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.AccountService;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.exception.InsufficientFundsException;
import ru.sinitsyn.service.model.AccountModel;
import ru.sinitsyn.service.model.ConvertedBalanceModel;
import ru.sinitsyn.service.model.OperationModel;
import ru.sinitsyn.service.model.RateModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{
    private final AccountDao accountDao;
    private final UserDao userDao;
    private final OperationDao operationDao;
    private final RateCacheService rateCacheService;

    public AccountServiceImpl(AccountDao accountDao, UserDao userDao, OperationDao operationDao, RateCacheService rateCacheService){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.operationDao = operationDao;
        this.rateCacheService = rateCacheService;
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

        return accountDao.findAllByUserId(id)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountModel> getAllAccounts() {
        return accountDao.findAll()
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ConvertedBalanceModel getBalanceInCurrency(Long accountId, String currency) {
        Account account = accountDao.findById(accountId);

        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        String normalizedCurrency = currency.toUpperCase();

        if (normalizedCurrency.equals("RUB")) {
            return new ConvertedBalanceModel(account.getId(), account.getBalance(), "RUB", BigDecimal.ONE, account.getBalance(), null);
    }
        RateModel rate = rateCacheService.getRate(normalizedCurrency)
                .orElseThrow(() -> new RuntimeException("No actual rate for currency:" + normalizedCurrency ));
        BigDecimal convertedBalance = account.getBalance().divide(rate.rateToRub(), 2, RoundingMode.HALF_UP);

        return new ConvertedBalanceModel(account.getId(), account.getBalance(), normalizedCurrency, rate.rateToRub(), convertedBalance, rate.timestamp());
}
}
