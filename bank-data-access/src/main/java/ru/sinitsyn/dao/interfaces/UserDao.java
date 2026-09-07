package ru.sinitsyn.dao.interfaces;

import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    User findById(Long id);
    User findByLogin(String login);
    List<User> findAll(HairColor hairColor, Gender gender);
    void delete(User user);

    List<User> findFriendsByUserId(Long id);
}
