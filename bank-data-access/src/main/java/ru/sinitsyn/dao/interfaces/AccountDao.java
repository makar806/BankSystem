package ru.sinitsyn.dao.interfaces;

import ru.sinitsyn.model.Account;

import java.util.List;

public interface AccountDao {
    Account save(Account account);
    Account findById(Long id);
    List<Account> findByOwnerId(Long id);
    List<Account> findAll();
    void deleteById(Long id);
}
