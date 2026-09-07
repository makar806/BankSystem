package ru.sinitsyn.dao;

import ru.sinitsyn.model.Account;

import java.util.List;

public interface AccountDao {
    Account save(Account account);
    Account findById(Long id);
    Account findByOwnerId(Long ownerId);
    List<Account> findAllByUserId(Long id);
    List<Account> findAll();
}
