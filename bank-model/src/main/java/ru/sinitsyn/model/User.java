package ru.sinitsyn.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
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
    private LocalDate birthDate;

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

    public User(String login, String name, LocalDate birthDate, HairColor hairColor, Gender gender){
        if (login == null || login.isBlank()){
            throw new IllegalArgumentException("Login can not be empty");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name can not be empty");
        }
        if (Period.between(birthDate, LocalDate.now()).getYears() < 14) {
            throw new IllegalArgumentException("You can't get bank account yet");
        }
        if (Period.between(birthDate, LocalDate.now()).getYears() > 110) {
            throw new IllegalArgumentException("Please specify correctly age");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё\\s-]+$")) {
            throw new IllegalArgumentException("Name can contain only letters, spaces and hyphens");
        }
        this.login = login;
        this.name = name;
        this.birthDate = birthDate;
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
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public LocalDate getBirthDate() { return  birthDate;}

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
