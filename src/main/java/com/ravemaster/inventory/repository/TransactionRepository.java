package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t.id FROM Transaction t")
    Page<Long> findAllTransactionIds(Pageable pageable);

    @Query("SELECT t FROM Transaction t LEFT JOIN FETCH t.transactionLines WHERE t.id IN :ids")
    List<Transaction> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT t FROM Transaction t JOIN FETCH t.transactionLines WHERE t.id = :id")
    Optional<Transaction> findByIdWithLines(@Param("id") UUID id);
}
