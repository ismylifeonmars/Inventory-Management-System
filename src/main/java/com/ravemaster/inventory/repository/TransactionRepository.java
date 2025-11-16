package com.ravemaster.inventory.repository;

import com.ravemaster.inventory.domain.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query("SELECT t.id FROM Transaction t")
    Page<UUID> findAllTransactionIds(Pageable pageable);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.transactionLines LEFT JOIN FETCH t.user u WHERE t.id IN :ids")
    List<Transaction> findByIds(@Param("ids") List<UUID> ids, Sort sort);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.transactionLines LEFT JOIN FETCH t.user u WHERE t.id IN :ids AND t.transactionType = :transactionType")
    List<Transaction> findByTransactionType(@Param("ids") List<UUID> ids,@Param("transactionType") String transactionType, Sort sort);

    @Query("SELECT DISTINCT t FROM Transaction t LEFT JOIN FETCH t.transactionLines LEFT JOIN FETCH t.user u WHERE t.id IN :ids AND t.saleType = :saleType")
    List<Transaction> findBySaleType(@Param("ids") List<UUID> ids,@Param("saleType") String saleType, Sort sort);

    @Query("SELECT DISTINCT t FROM Transaction t JOIN FETCH t.transactionLines LEFT JOIN FETCH t.user u WHERE t.id = :id")
    Optional<Transaction> findByIdWithLines(@Param("id") UUID id);

    @Query("""
           SELECT COALESCE(SUM(t.totalAmount),0)
           FROM Transaction t
           WHERE t.transactionType = :transactionType
           AND t.createdAt >= :startOfDay
           AND t.createdAt < :endOfDay
           """)
    BigDecimal calculateTotalSalesPerDay(
            @Param("transactionType") String transactionType,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
           SELECT COALESCE(SUM(t.totalAmount),0)
           FROM Transaction t
           WHERE t.transactionType = :transactionType
           AND t.saleType = :saleType
           AND t.createdAt >= :startOfDay
           AND t.createdAt < :endOfDay
           """)
    BigDecimal calculateTotalSalesPerDayPerType(
            @Param("transactionType") String transactionType,
            @Param("saleType") String saleType,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
