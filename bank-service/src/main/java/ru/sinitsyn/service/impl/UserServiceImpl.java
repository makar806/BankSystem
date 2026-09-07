package ru.sinitsyn.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.dao.interfaces.UserDao;
import ru.sinitsyn.dao.repositories.AuthUserRepository;
import ru.sinitsyn.model.*;
import ru.sinitsyn.service.DomainMapper;
import ru.sinitsyn.service.UserService;
import ru.sinitsyn.service.model.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserRepository authUserRepository;
    private final AccountDao accountDao;
    private final OperationDao operationDao;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, AuthUserRepository authUserRepository, AccountDao accountDao, OperationDao operationDao){
        this.userDao = userDao;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountDao = accountDao;
        this.operationDao = operationDao;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8){
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")){
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[!\\-+_?%$#].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character: ! - + _ ? % $ #");
        }
    }

    @Override
    public UserModel creatUser(String login, String name, HairColor hairColor, Gender gender, LocalDate birthDate, String password){
        validatePassword(password);
        User existingUser = userDao.findByLogin(login);
        if (existingUser != null) {
            throw new RuntimeException("User with this login is already exist!!!!!");
        }
        if (authUserRepository.findByLogin(login).isPresent()) {
            throw new RuntimeException("This user already has auth");
        }

        User user = new User(login, name, birthDate, hairColor, gender);
        User savedUser = userDao.save(user);

        AuthUser authUser = new AuthUser(login, passwordEncoder.encode(password), Role.CLIENT, savedUser);
        authUserRepository.save(authUser);
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
    public void addFriend(Long userId, Long friendId, String currentUserLogin){
        User user = userDao.findById(userId);
        User friend = userDao.findById(friendId);

        if (!user.getLogin().equals(currentUserLogin)) {
            throw new RuntimeException("You can add friends just to you");
        }

        if (user.getFriends().contains(friend)) {
            throw new RuntimeException("you already have this friend");
        }

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
    public void removeUser(Long id) {
        User user = userDao.findById(id);
        List<Account> accounts = accountDao.findByOwnerId(id);
        if (user == null) {
            throw new RuntimeException("no user with this ID was found");
        }

        for (Account account : accounts) {
            operationDao.deleteByAccountId(account.getId());
            accountDao.deleteById(account.getId());
        }

        authUserRepository.deleteByClientId(id);

        List<User> allUsers = userDao.findAll(null, null);
        for (User otherUser : allUsers) {
            otherUser.removeFriend(user);
            userDao.save(otherUser);
        }

        userDao.delete(user);
    }

    @Override
    public void removeFriend(Long userId, Long friendId, String currentUserLogin){
        User user = userDao.findById(userId);
        User friend = userDao.findById(friendId);

        if (!user.getFriends().contains(friend)) {
            throw new RuntimeException("You don't have this friend");
        }

        if (!user.getLogin().equals(currentUserLogin)) {
            throw new RuntimeException("You can delete just your friends");
        }

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
