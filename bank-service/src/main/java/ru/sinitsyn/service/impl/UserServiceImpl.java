package ru.sinitsyn.service.impl;

import ru.sinitsyn.dao.UserDao;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public User creatUser(String login, String name, HairColor hairColor, Gender gender, Integer age){
        User existingUser = userDao.findByLogin(login);
        if (existingUser != null){
            throw new RuntimeException("User with this login is already exist!!!!!");
        }
        User user = new User(login, name, age, hairColor, gender);
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id){
        User user = userDao.findById(id);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId){
        User user = userDao.findById(userId);
        User friend = userDao.findById(friendId);

        if (user == null || friend == null){
            throw new RuntimeException("User or friend not found");
        }

        if (user.getId().equals(friend.getId())){
            throw new RuntimeException("You can not ass yourself to your friend");
        }

        user.addFriend(friend);
        friend.addFriend(user);

        userDao.save(user);
        userDao.save(friend);
    }

    @Override
    public void removeFriend(Long userId, Long friendId){
        User user = userDao.findById(userId);
        User friend = userDao.findById(friendId);

        if (user == null || friend == null){
            throw new RuntimeException("User or friend not found");
        }

        user.removeFriend(friend);
        friend.removeFriend(user);

        userDao.save(user);
        userDao.save(friend);
    }

}
