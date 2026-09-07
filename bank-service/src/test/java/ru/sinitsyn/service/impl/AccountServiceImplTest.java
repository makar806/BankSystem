package ru.sinitsyn.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sinitsyn.dao.AccountDao;
import ru.sinitsyn.dao.OperationDao;
import ru.sinitsyn.dao.UserDao;
import ru.sinitsyn.model.*;
import ru.sinitsyn.service.TransferService;
import ru.sinitsyn.service.exception.InsufficientFundsException;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountDao accountDao;

    @Mock
    private UserDao userDao;

    @Mock
    private OperationDao operationDao;

    @Mock
    private RateCacheService rateCacheService;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp(){
        accountService = new AccountServiceImpl(accountDao, userDao, operationDao, rateCacheService);
    }

    @Test
    void deposit_shouldIncreaseBalanceAndSaveOperation() {
        User user = new User("login1", "Makar", 19, HairColor.BLACK, Gender.MALE);
        Account account = new Account(new BigDecimal("100"), user);

        when(accountDao.findById(1L)).thenReturn(account);

        accountService.deposit(1L, new BigDecimal("50"));
        assertEquals(new BigDecimal("150"), account.getBalance());
        verify(accountDao).save(account);

        ArgumentCaptor<Operation> operationArgumentCaptor = ArgumentCaptor.forClass(Operation.class);
        verify(operationDao).save(operationArgumentCaptor.capture());

        Operation saveOperation = operationArgumentCaptor.getValue();
        assertEquals(OperationType.DEPOSIT, saveOperation.getType());
        assertEquals(new BigDecimal("50"), saveOperation.getAmount());
        assertEquals(account, saveOperation.getAccount());

    }

    @Test
    void withdraw_shouldDecreaseBalanceAndSaveOperation() {
        User user = new User("login2", "Egor", 20, HairColor.BLONDE, Gender.MALE);
        Account account = new Account(new BigDecimal("200"), user);

        when(accountDao.findById(1L)).thenReturn(account);

        accountService.withdraw(1L, new BigDecimal("70"));

        assertEquals(new BigDecimal("130"), account.getBalance());
        verify(accountDao).save(account);

        ArgumentCaptor<Operation> operationArgumentCaptor = ArgumentCaptor.forClass(Operation.class);
        verify(operationDao).save(operationArgumentCaptor.capture());

        Operation saveOperation = operationArgumentCaptor.getValue();
        assertEquals(OperationType.WITHDRAW, saveOperation.getType());
        assertEquals(new BigDecimal("70"), saveOperation.getAmount());
        assertEquals(account, saveOperation.getAccount());
    }

    @Test
    void withdraw_shouldThrowInsufficientFundsExceptionWhenBalanceIsNotEnough() {
        User user = new User("login3", "Ivan", 21, HairColor.BLONDE, Gender.MALE);
        Account account = new Account(new BigDecimal("30"), user);

        when(accountDao.findById(1L)).thenReturn(account);

        assertThrows(InsufficientFundsException.class, () -> accountService.withdraw(1L, new BigDecimal("100")));

        verify(accountDao, never()).save(any());
        verify(operationDao, never()).save(any());
    }

    @Test
    void transfer_shouldThrowInblablablaWhenBalanceIsNotEnough() {
        User fromUser = new User("login6", "Makar", 19, HairColor.BLACK, Gender.MALE);
        User toUser = new User("login7", "Ivan", 21, HairColor.PINK,Gender.MALE);

        setUserId(fromUser, 1L);
        setUserId(toUser, 2L);

        Account fromAccount = new Account(new BigDecimal("50"), fromUser);
        Account toAccount = new Account(new BigDecimal("100"), toUser);
        TransferService transferService = new TransferServiceImpl(accountDao, operationDao);

        when(accountDao.findById(1L)).thenReturn(fromAccount);
        when(accountDao.findById(2L)).thenReturn(toAccount);

        assertThrows(InsufficientFundsException.class, () -> transferService.transfer(1L,2L, new BigDecimal("100")));

        verify(accountDao, never()).save(any());
        verify(operationDao,never()).save(any());


    }
    private void setUserId(User user, Long id) {
        try {
            Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
