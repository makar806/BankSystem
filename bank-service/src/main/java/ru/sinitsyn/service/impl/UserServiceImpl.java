package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.UserDao;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.UserService;
import ru.sinitsyn.service.model.UserModel;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao){
        this.userDao = userDao;
    }

    @Override
    public UserModel creatUser(String login, String name, HairColor hairColor, Gender gender, Integer age){
        User existingUser = userDao.findByLogin(login);
        if (existingUser != null){
            throw new RuntimeException("User with this login is already exist!!!!!");
        }
        User user = new User(login, name, age, hairColor, gender);
        User savedUser = userDao.save(user);
        return DomainMapper.toModel(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel getUserById(Long id){
        User user = userDao.findById(id);
        if (user == null) {
            throw new RuntimeException("user not found");
        }
        return DomainMapper.toModel(user);
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

    @Override
    public List<UserModel> getAllUsers(HairColor hairColor, Gender gender){
        return userDao.findAll(hairColor, gender)
                .stream()
                .map(DomainMapper::toModel)
                .toList();
    }

    @Override
    public Set<UserModel> getFriendByUserId(Long id){
        User user = userDao.findById(id);
        if (user == null){
            throw new RuntimeException("user not found");
        }
        return user.getFriends()
                .stream()
                .map(DomainMapper::toModel)
                .collect(Collectors.toSet());
    }



}
