package ru.sinitsyn.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.sinitsyn.model.User;

public class HibernateUserDao implements UserDao{

    private final SessionFactory sessionFactory;

    public HibernateUserDao(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user){
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            if (user.getId() == null){
                session.persist(user);
            }
            else {
                user = session.merge(user);
            }

            transaction.commit();
            return user;

        }
        catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public User findById(Long id){
        try (Session session = sessionFactory.openSession()){
            return session.get(User.class, id);
        }
    }

    @Override
    public User findByLogin(String login){
        try (Session session = sessionFactory.openSession()){
            return session.createQuery("FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }
}
