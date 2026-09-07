package ru.sinitsyn.dao.JpaRepositoryDao;

import org.springframework.stereotype.Repository;
import ru.sinitsyn.dao.interfaces.UserDao;
import ru.sinitsyn.dao.repositories.UserRepository;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

import java.util.List;

@Repository
public class JpaRepositoryUserDao implements UserDao {

    private UserRepository userRepository;

    public JpaRepositoryUserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User findByLogin(String login){
        return userRepository.findByLogin(login).orElse((null));
    }

    @Override
    public List<User> findAll(HairColor hairColor, Gender gender) {
        return userRepository.findAll(hairColor, gender);
    }

    @Override
    public List<User> findFriendsByUserId(Long id) {
        return userRepository.findFriendsByUserId(id);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }


}
