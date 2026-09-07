package ru.sinitsyn.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sinitsyn.model.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByLogin(String login);
    void deleteByClientId(Long clientId);
}
