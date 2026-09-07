package ru.sinitsyn.app.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sinitsyn.dao.repositories.AuthUserRepository;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthUserRepository authUserRepository;

    public CustomUserDetailsService(AuthUserRepository authUserRepository){
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var authUser = authUserRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Auth user not found"));
        return new org.springframework.security.core.userdetails.User(authUser.getLogin(), authUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + authUser.getRole().name())));
    }
}
