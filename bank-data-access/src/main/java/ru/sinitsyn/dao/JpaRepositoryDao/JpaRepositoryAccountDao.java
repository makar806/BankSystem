package ru.sinitsyn.dao.JpaRepositoryDao;

import org.springframework.stereotype.Repository;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.dao.repositories.AccountRepository;
import ru.sinitsyn.model.Account;

import java.util.List;

@Repository
public class JpaRepositoryAccountDao implements AccountDao {
    public AccountRepository accountRepository;

    public JpaRepositoryAccountDao(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id){
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("No Account with this id"));
    }

    @Override
    public List<Account> findByOwnerId(Long id){
        return accountRepository.findByOwnerId(id);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {accountRepository.deleteById(id);}


}
