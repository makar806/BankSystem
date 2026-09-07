package ru.sinitsyn.service.impl;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsyn.dao.repositories.AuthUserRepository;
import ru.sinitsyn.model.AuthUser;
import ru.sinitsyn.model.Role;
import ru.sinitsyn.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
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
    public void createAdmin(String login, String password){
        validatePassword(password);

        if (authUserRepository.findByLogin(login).isPresent()){
            throw new IllegalArgumentException("Admin with this login already exists");
        }

        AuthUser admin = new AuthUser(login, passwordEncoder.encode(password), Role.ADMIN, null);
        authUserRepository.save(admin);
    }

    @Override
    public void deleteAdmin(String login, String currentAdminLogin) {
        AuthUser admin = authUserRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("There is no admin with this login"));
        AuthUser currentAdmin = authUserRepository.findByLogin(currentAdminLogin).orElseThrow(() -> new RuntimeException("You must be auth like admin"));

        if (currentAdmin.getId().equals(admin.getId())) {
            throw new RuntimeException("You can't delete yourself");
        }

        if (admin.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("This auth user is not admin");
        }

        authUserRepository.delete(admin);

    }

}
