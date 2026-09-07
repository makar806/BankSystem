package ru.sinitsyn.dao;

import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    User findById(Long id);
    User findByLogin(String login);
    List<User> findAll(HairColor hairColor, Gender gender);

    List<User> findFriendsByUserId(Long id);
}
