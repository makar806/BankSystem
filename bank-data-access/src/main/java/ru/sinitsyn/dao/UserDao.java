package ru.sinitsyn.dao;

import ru.sinitsyn.model.User;

public interface UserDao {
    User save(User user);
    User findById(Long id);
    User findByLogin(String login);
}
