package ru.sinitsyn.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sinitsyn.model.Operation;
import ru.sinitsyn.model.OperationType;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    void deleteByAccountId(Long id);

    @Query("""
        select distinct o
        from Operation o
        where o.account.id = :accountId""")
    List<Operation> findByAccountId(@Param("accountId") Long id);


    @Query("""
        select distinct o
        from Operation o
        where (:operationType is null or o.type = :operationType)
        and (:accountId is null or o.account.id = :accountId)""")
    List<Operation> findAll(@Param("operationType") OperationType operationType, @Param("accountId") Long id);

}
