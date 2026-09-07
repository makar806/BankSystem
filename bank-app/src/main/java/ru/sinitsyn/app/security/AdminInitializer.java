package ru.sinitsyn.app.security;

import jakarta.persistence.Column;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.sinitsyn.dao.repositories.AuthUserRepository;
import ru.sinitsyn.model.AuthUser;
import ru.sinitsyn.model.Role;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) {
        if (authUserRepository.findByLogin("admin").isEmpty()) {
            AuthUser admin = new AuthUser("admin", passwordEncoder.encode("admin"), Role.ADMIN, null);
            authUserRepository.save(admin);
        }
    }
}
