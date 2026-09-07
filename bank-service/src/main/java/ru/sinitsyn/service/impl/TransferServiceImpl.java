package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.TransferService;
import ru.sinitsyn.service.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private final AccountDao accountDao;
    private final OperationDao operationDao;

    public TransferServiceImpl(AccountDao accountDao, OperationDao operationDao){
        this.accountDao = accountDao;
        this.operationDao = operationDao;
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String currentUserLogin){
        Account fromAccount = accountDao.findById(fromAccountId);
        Account toAccount = accountDao.findById(toAccountId);

        if (!fromAccount.getOwner().getLogin().equals(currentUserLogin)) {
            throw new RuntimeException("You can make transfer just from your account");
        }

        if (Objects.equals(fromAccountId, toAccountId)) {
            throw new RuntimeException("You can't make transfers between 1 account");
        }

        if (fromAccount == null || toAccount == null){
            throw new RuntimeException("Account not found");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw  new RuntimeException("Amount must be greater then zero");
        }

        BigDecimal commissionRate = getCommissionRate(fromAccount.getOwner(), toAccount.getOwner());
        BigDecimal commission = amount.multiply(commissionRate);
        BigDecimal totalAmount = amount.add(commission);

        if (fromAccount.getBalance().compareTo(totalAmount) < 0){
            throw new InsufficientFundsException("No money");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(totalAmount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountDao.save(fromAccount);
        accountDao.save(toAccount);

        Operation withdrawOperation = new Operation(amount, OperationType.TRANSFER_OUT, fromAccount);
        Operation depositOperation = new Operation(amount, OperationType.TRANSFER_IN, toAccount);

        operationDao.save(withdrawOperation);
        operationDao.save(depositOperation);

    }

    private BigDecimal getCommissionRate(User fromUser, User toUser) {
        if (fromUser.getId().equals(toUser.getId())){
            return BigDecimal.ZERO;
        }

        if (fromUser.getFriends().contains(toUser)){
            return new BigDecimal("0.03");
        }

        return new BigDecimal("0.10");
    }
}
