package ru.sinitsyn.service;

import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

public interface UserService {
    User creatUser(String login, String name, HairColor hairColor, Gender gender, Integer age);
    User getUserById(Long id);
    void addFriend(Long userId, Long friendId);
    void removeFriend(Long userId, Long friendId);
}
