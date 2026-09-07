package ru.sinitsyn.dao;

import ru.sinitsyn.model.Account;

public interface AccountDao {
    Account save(Account account);
    Account findById(Long id);
    Account findByOwnerId(Long ownerId);
}
