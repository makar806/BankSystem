package ru.sinitsyn.dao.Hibernate;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.sinitsyn.dao.interfaces.AccountDao;
import ru.sinitsyn.model.Account;

import java.util.List;

public class HibernateAccountDao implements AccountDao {
    private final EntityManager entityManager;

    public HibernateAccountDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Account save(Account account){

           if (account.getId() == null){
               entityManager.persist(account);
               return account;
            }
            else {
                return entityManager.merge(account);
            }
    }

    @Override
    public Account findById(Long id){
        return entityManager.find(Account.class, id);

    }


    @Override
    public List<Account> findByOwnerId(Long id){
        return entityManager.createQuery("""
            FROM Account a
            WHERE a.owner.id = :id
            """, Account.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Account> findAll() {
        return entityManager.createQuery("""
                                    select a
                                    from Account a
                                    join fetch a.owner""", Account.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {}
}
