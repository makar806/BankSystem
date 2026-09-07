package ru.sinitsyn.service;

import ru.sinitsyn.model.Account;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.model.UserModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface UserService {
    UserModel creatUser(String login, String name, HairColor hairColor, Gender gender, LocalDate birthDate, String password);
    UserModel getUserById(Long id);
    void addFriend(Long userId, Long friendId, String currentUserLogin);
    void removeFriend(Long userId, Long friendId, String currentUserLogin);
    void removeUser(Long id);

    List<UserModel> getAllUsers(HairColor hairColor, Gender gender);
    Set<UserModel> getFriendByUserId(Long id);
}
