package ru.sinitsyn.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Operation;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByOwnerId(Long id);

    void deleteById(Long id);
}
