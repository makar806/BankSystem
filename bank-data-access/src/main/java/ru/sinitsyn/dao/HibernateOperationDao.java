package ru.sinitsyn.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.sinitsyn.model.Operation;

import java.util.List;

public class HibernateOperationDao implements OperationDao {
    private final SessionFactory sessionFactory;

    public HibernateOperationDao(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Operation save(Operation operation){
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();

            if (operation.getId() == null) {
                session.persist(operation);
            }
            else {
                operation = session.merge(operation);
            }

            transaction.commit();
            return operation;

        }

        catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Operation> findByAccountId(Long accountId){
        try (Session session = sessionFactory.openSession()){
            return session.createQuery(
                    "FROM Operation o WHERE o.account.id = :accountId",
                    Operation.class
            )
                    .setParameter("accountId", accountId)
                    .getResultList();
        }
    }
}
