package ru.sinitsyn.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.sinitsyn.model.Gender;
import ru.sinitsyn.model.HairColor;
import ru.sinitsyn.model.User;

import java.util.List;


@Repository
public class HibernateUserDao implements UserDao{

    private final EntityManager entityManager;

    public HibernateUserDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public User save(User user){

        if (user.getId() == null){
            entityManager.persist(user);
            return user;
            }
        else {
                return entityManager.merge(user);
            }
    }

    @Override
    public User findById(Long id){
        return entityManager.createQuery("""
                            select distinct u
                            from User u
                            left join fetch u.friends
                            where u.id = :id
                            """, User.class).setParameter("id", id).getResultStream().findFirst().orElse(null);
    }

    @Override
    public User findByLogin(String login){
        return entityManager.createQuery("FROM User u WHERE u.login = :login", User.class)
                .setParameter("login", login)
                .getResultStream()
                .findFirst()
                .orElse(null);

    }

    @Override
    public List<User> findAll(HairColor hairColor, Gender gender){

            StringBuilder request = new StringBuilder("""
                    select distinct u
                    from User u
                    left join fetch u.friends
                    WHERE 1=1
                    """);

            if (hairColor != null) {
                request.append(" AND u.hairColor = :hairColor");
            }

            if (gender != null){
                request.append(" AND u.gender = :gender");
            }

            var query = entityManager.createQuery(request.toString(), User.class);

            if (hairColor != null) {
                query.setParameter("hairColor", hairColor);
            }

            if (gender != null) {
                query.setParameter("gender", gender);
            }

            return query.getResultList();
        }



    @Override
    public List<User> findFriendsByUserId(Long id){

        return entityManager.createQuery(
                """
                select f
                from User u 
                join u.friends f
                where u.id = :userId
                """, User.class).setParameter("userId", id).getResultList();
    }
}


