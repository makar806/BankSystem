package ru.sinitsyn.service;

import org.springframework.security.core.Authentication;

public interface AuthService {
    void createAdmin(String login, String password);
    void deleteAdmin(String login, String currentAdminLogin);
}
