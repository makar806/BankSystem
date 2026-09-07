package ru.sinitsyn.service;

import java.math.BigDecimal;

public interface TransferService {
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
