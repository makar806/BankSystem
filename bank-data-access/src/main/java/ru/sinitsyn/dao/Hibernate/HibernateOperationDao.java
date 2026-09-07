package ru.sinitsyn.dao.Hibernate;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.sinitsyn.dao.interfaces.OperationDao;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;

import java.util.List;


public class HibernateOperationDao implements OperationDao {
    private final EntityManager entityManager;

    public HibernateOperationDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Operation save(Operation operation){

        if (operation.getId() == null) {
            entityManager.persist(operation);
            return operation;
        }
        else {
            return entityManager.merge(operation);
        }


    }

    @Override
    public List<Operation> findByAccountId(Long accountId){

        return entityManager.createQuery("FROM Operation o WHERE o.account.id = :accountId", Operation.class)
                    .setParameter("accountId", accountId)
                    .getResultList();
    }

    @Override
    public List<Operation> findAll(OperationType type, Long id){

        StringBuilder request = new StringBuilder("FROM Operation o WHERE 1=1");

        if (type != null) {
            request.append(" AND o.type = :type");
        }

        if (id != null){
            request.append(" AND o.account.id = :id");
        }

        var query = entityManager.createQuery(request.toString(), Operation.class);

        if (type != null){
            query.setParameter("type", type);
        }

        if (id != null){
            query.setParameter("id", id);
        }

        return query.getResultList();
    }

    @Override
    public void deleteByAccountId(Long id) {}


}

