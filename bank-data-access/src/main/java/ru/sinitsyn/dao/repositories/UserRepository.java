package ru.sinitsyn.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);

    @Query("""
    select distinct a
    from User a
    where (:hairColor is null or a.hairColor = :hairColor)
    and (:gender is null or a.gender = :gender)""")
    List<User> findAll(@Param("hairColor") HairColor hairColor, @Param("gender") Gender gender);

    @Query("""
    select distinct f
    from User u
    join u.friends f
    where u.id = :userId""" )
    List<User> findFriendsByUserId(@Param("userId") Long id);
}
