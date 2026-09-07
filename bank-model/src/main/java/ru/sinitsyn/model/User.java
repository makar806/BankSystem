package ru.sinitsyn.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HairColor hairColor;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    public User(){}

    public User(String login, String name, Integer age, HairColor hairColor, Gender gender){
        if (login == null || login.isBlank()){
            throw new IllegalArgumentException("Login can not be empty");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name can not be empty");
        }
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Integer getAge(){
        return age;
    }

    public String getName() {return name;}
    public Gender getGender() {return gender;}
    public HairColor getHairColor() {return hairColor;}
    public Set<User> getFriends() {return friends;}

    public void setLogin(String login){
        this.login = login;
    }

    public void addFriend(User friend){
        if (friend != null && !this.equals(friend)){
            friends.add(friend);
        }
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
