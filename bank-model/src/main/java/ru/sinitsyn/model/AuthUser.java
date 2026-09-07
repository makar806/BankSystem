package ru.sinitsyn.model;

import jakarta.persistence.*;

@Entity
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "client_id")
    private User client;

    public AuthUser() {}

    public AuthUser(String login, String password, Role role, User client) {

        this.login = login;
        this.password =password;
        this.role = role;
        this.client = client;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {return password;}
    public Role getRole() {return role;}
    public Long getId() {return id;}
}
