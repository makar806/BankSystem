package ru.sinitsyn.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Security;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs",
                        "/v3/api-docs/**").permitAll()
                        .requestMatchers("POST", "/users").hasRole("ADMIN")
                        .requestMatchers("POST", "/accounts").hasRole("ADMIN")
                        .requestMatchers("GET", "/users").hasRole("ADMIN")
                        .requestMatchers("GET", "/users/*").hasRole("ADMIN")
                        .requestMatchers("GET", "/users/*/accounts").hasRole("ADMIN")
                        .requestMatchers("GET", "/accounts").hasRole("ADMIN")
                        .requestMatchers("GET", "/operations").hasRole("ADMIN")
                        .requestMatchers("POST", "/admins/**").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/users/*").hasRole("ADMIN")
                        .requestMatchers("DELETE", "/accounts/*").hasRole("ADMIN")

                        .requestMatchers("POST", "/users/*/friends/*").hasRole("CLIENT")
                        .requestMatchers("DELETE", "/users/*/friends/*").hasRole("CLIENT")
                        .requestMatchers("POST", "/accounts/*/deposit").hasRole("CLIENT")
                        .requestMatchers("POST", "/accounts/*/withdraw").hasRole("CLIENT")
                        .requestMatchers("GET", "/accounts/*/balance").hasRole("CLIENT")
                        .requestMatchers("POST", "/transfer").hasRole("CLIENT")

                        .anyRequest().authenticated()

                ).httpBasic(Customizer.withDefaults()).build();
    }
}
