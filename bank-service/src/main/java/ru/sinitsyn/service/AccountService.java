package ru.sinitsyn.service;

import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.service.model.AccountModel;
import ru.sinitsyn.service.model.OperationModel;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountModel createAccount(Long userId);
    BigDecimal getBalance(Long accountId);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
    List<OperationModel> getOperations(Long accountId);
    List<AccountModel> getAccountsByUserId(Long id);
    List<AccountModel> getAllAccounts();
}
