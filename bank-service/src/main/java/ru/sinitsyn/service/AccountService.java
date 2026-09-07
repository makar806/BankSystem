package ru.sinitsyn.service;

import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account createAccount(Long userId);
    BigDecimal getBalance(Long accountId);
    void deposit(Long accountId, BigDecimal amount);
    void withdraw(Long accountId, BigDecimal amount);
    List<Operation> getOperations(Long accountId);
}
