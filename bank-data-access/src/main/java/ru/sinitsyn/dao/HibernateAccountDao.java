package ru.sinitsyn.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.sinitsyn.model.Account;

public class HibernateAccountDao implements AccountDao{
    private final SessionFactory sessionFactory;

    public HibernateAccountDao(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Account save(Account account){
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            if (account.getId() == null){
                session.persist(account);
            }
            else {
                account = session.merge(account);
            }

            transaction.commit();
            return account;
        }
        catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Account findById(Long id){
        try (Session session = sessionFactory.openSession()){
            return session.get(Account.class, id);
        }
    }

    @Override
    public Account findByOwnerId(Long ownerId){
        try (Session session = sessionFactory.openSession()){
            return session.createQuery("FROM Account a WHERE a.owner.id = :ownerId", Account.class)
                    .setParameter("ownerId", ownerId)
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }
}
